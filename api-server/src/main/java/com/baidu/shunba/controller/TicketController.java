package com.baidu.shunba.controller;

import com.baidu.shunba.bean.*;
import com.baidu.shunba.bean.message.TicketRequestMessage;
import com.baidu.shunba.client.SocketServerInnerApiClient;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.entity.SBShift;
import com.baidu.shunba.entity.SBTicket;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import com.baidu.shunba.queryvo.SBTicketQueryVO;
import com.baidu.shunba.service.SBShiftService;
import com.baidu.shunba.service.SBTicketService;
import com.baidu.shunba.sync.ShunbaSyncService;
import com.baidu.shunba.vo.*;
import com.baidu.shunba.utils.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("ticket")
public class TicketController {
    @Autowired
    private SBTicketService sbTicketService;

    @Autowired
    private SBShiftService sbShiftService;

    @Autowired
    private ShunbaSyncService shunbaSyncService;

    @Autowired
    private SocketServerInnerApiClient socketServerInnerApiClient;

    @ApiOperation("")
    @RequestMapping(value = "listTicket", method = RequestMethod.POST)
    public ResultVo listTicket(@RequestBody QueryVo<SBTicketQueryVO> query) {
        return ResultVo.ok(sbTicketService.commonQuery(query));
    }

    @ApiOperation("")
    @RequestMapping(value = "pullTicket", method = RequestMethod.GET)
    public ResultVo pullTicket() {
        final Date now = new Date();
        try {
            List<SBShift> shifts = sbShiftService.findAllBySeq(DateUtils.getYearMonthDaySeq(now));
            for (SBShift shift : shifts) {
                RequestThirdTicket req = new RequestThirdTicket();
                req.setTransitDate(new Date());
                req.shiftID = shift.getShiftId();
                // TODO
                String response = HttpUtils.fetchHttpUrlResponse("", HttpUtils.ContentType.JSON, "");
                ThirdTicketObject ticketObject = GsonUtils.fromJson(response, ThirdTicketObject.class);
                processThirdTickets(ticketObject);
            }
            return ResultVo.ok("拉取完成");
        } catch (Exception e) {
            return ResultVo.error(e);
        }
    }

    private Collection<SBTicket> processThirdTickets(ThirdTicketObject ticketObject) throws AppException {
        //1
        Map<String, SBTicket> newTickets = new HashMap<>();
        for (ThirdTicket ticket : ticketObject.ticketAry) {
            SBTicket sbt = new SBTicket();
            sbt.setTicketNo(ticket.ticketID);
            sbt.setSeq(DateUtils.getYearMonthDaySeq(DateUtils.getDateFromStringWithFormat(ticket.transitDate, "yyyy-MM-dd")));

            //1
            if (StringUtils.isNotEmpty(ticket.faceID)) {
                sbt.setMemberId(ticket.faceID);
            }
            if (StringUtils.isNotEmpty(ticket.operate)) {
                sbt.setDelFlag(ObjectUtils.getIntValue(ticket.operate));
            }

            //2
            if (StringUtils.isNotEmpty(ticket.checkResult)) {
                sbt.setIsCheck(ObjectUtils.getBooleanValue(ticket.checkResult) ? 1 : 0);
            }
            if (StringUtils.isNotEmpty(ticket.checkType)) {
                sbt.setCheckType(ObjectUtils.getIntValue(ticket.checkType));
            }
            if (StringUtils.isNotEmpty(ticket.temperature)) {
                sbt.setTemperature(ObjectUtils.getDoubleValue(ticket.temperature));
            }

            //3
            if (StringUtils.isNotEmpty(ticket.shiftID)) {
                sbt.setShiftId(ticket.shiftID);
            } else {
                sbt.setShiftId(ticketObject.shiftID);
            }
            sbt.setLineId(ticketObject.lineID);

            newTickets.put(sbt.getTicketNo(), sbt);
        }

        //2
        SBTicketQueryVO vo = new SBTicketQueryVO();

        StringFilter filter = new StringFilter();
        filter.setIn(newTickets.keySet());
        vo.setTicketNo(filter);

        List<SBTicket> tickets = sbTicketService.findAll(vo);

        Map<String, SBTicket> oldTickets = new HashMap<>();
        for (SBTicket sbt : tickets) {
            oldTickets.put(sbt.getTicketNo(), sbt);
        }

        //3
        for (String ticketId : newTickets.keySet()) {
            SBTicket newT = newTickets.get(ticketId);
            SBTicket oldT = oldTickets.get(ticketId);
            if (oldT == null) {
                oldTickets.put(ticketId, newT);
            } else {
                if (ObjectUtils.getIntValue(oldT.getIsCheck()) == 1 && ObjectUtils.getIntValue(newT.getIsCheck()) != 1) {
                    // tsLogService.addLog("服务器返回未核销票据，但是本地已核销:" + GsonUtils.toJson(oldT) + "," + GsonUtils.toJson(newT), 1, 1);
                }
                try {
                    MyBeanUtils.copyBeanNotNull2Bean(newT, oldT);
                } catch (Exception e) {
                }
            }
        }

        sbTicketService.saveAll(oldTickets.values());

        return oldTickets.values();
    }

    @ApiOperation("")
    @RequestMapping(value = "pullTicketResult", method = RequestMethod.GET)
    public ResultVo pullTicketResult() {
        final Date now = new Date();
        try {
            List<SBTicket> shifts = sbTicketService.findAllBySeq(DateUtils.getYearMonthDaySeq(now));
            for (SBTicket shift : shifts) {
                RequestPullThirdTicketResult req = new RequestPullThirdTicketResult();
                req.setTransitDate(new Date());
                req.shiftID = shift.getShiftId();
                // TODO
                String response = HttpUtils.fetchHttpUrlResponse("", HttpUtils.ContentType.JSON, "");
                ThirdTicketObject ticketObject = GsonUtils.fromJson(response, ThirdTicketObject.class);
                processThirdTickets(ticketObject);
            }
            return ResultVo.ok("拉取完成");
        } catch (Exception e) {
            return ResultVo.error(e);
        }
    }

    @ApiOperation("")
    @RequestMapping(value = "pushTicketResult", method = RequestMethod.GET)
    public ResultVo pushTicketResult() {
        try {
            final Date now = new Date();
            final int seq = DateUtils.getYearMonthDaySeq(now);
            final int pageSize = 300;
            int curPage = 0;

            SBTicketQueryVO vo = new SBTicketQueryVO();

            IntegerFilter isCheck = new IntegerFilter();
            isCheck.setEquals(1);
            vo.setIsCheck(isCheck);

            IntegerFilter seqFilter = new IntegerFilter();
            seqFilter.setEquals(seq);
            vo.setSeq(seqFilter);

            Map<String, String> sort = new HashMap<>();
            sort.put("createDate", "asc");

            while (true) {
                Pageable pageable = new PageVo(curPage, pageSize, sort).toPageable();
                Page<SBTicket> page = sbTicketService.findByPage(vo, pageable);

                List<SBTicket> sbTickets = page.getContent();

                if (sbTickets.isEmpty()) {
                    break;
                }

                curPage++;

                RequestPushThirdTicketResult req = new RequestPushThirdTicketResult();
                req.setTransitDate(new Date());

                for (SBTicket ticket : sbTickets) {
                    ThirdTicket tTicket = new ThirdTicket();
                    tTicket.ticketID = ticket.getTicketNo();
                    tTicket.checkResult = ObjectUtils.getIntValue(ticket.getIsCheck()) == 1 ? "true" : "false";
                    tTicket.temperature = ObjectUtils.getString(ticket.getTemperature());
                    tTicket.checkType = ObjectUtils.getString(ticket.getCheckType());

                    req.ticketAry.add(tTicket);
                }

                // TODO
                String response = HttpUtils.fetchHttpUrlResponse("", HttpUtils.ContentType.JSON, "");
                ThirdTicketObject ticketObject = GsonUtils.fromJson(response, ThirdTicketObject.class);
            }
            return ResultVo.ok("推送完成");
        } catch (Exception e) {
            log.error("推送失败:", e);
            return ResultVo.error(e);
        }
    }

    @ApiOperation("pad推送核票结果")
    @RequestMapping(value = "submitTicketResult", method = {RequestMethod.POST})
    public ResultVo<String> submitTicketResult(@RequestBody List<SBTicket> tickets) throws AppException {
        log.info("收到核票数据: {}", tickets);

        if (null == tickets || tickets.size() == 0) {
            return ResultVo.ok(ResultEnum.SUCCESS, "传入的票据为空");
        }

        // 存储票据
        List<SBTicket> sbTickets = sbTicketService.saveTicketResult(tickets);

        // 同步票据到顺吧
        shunbaSyncService.pushTicket(sbTickets);

        // 返回结果
        return ResultVo.ok(ResultEnum.SUCCESS, "成功处理条" + tickets.size() + "记录");
    }

    @ApiOperation("顺吧推送票据")
    @RequestMapping(value = "receiveTickets", method = {RequestMethod.POST})
    public ResponseMessage receiveTickets(@RequestBody TicketRequestMessage message) {
        Collection<SBTicket> sbTickets = null;
        try {
            sbTickets = sbTicketService.receiveTickets(message);
        } catch (AppException exception) {
            return ResponseMessage.businessError(message.getTransSn());
        }

        Map<String, Map<String, List<String>>> pushTicketsBeans = new HashMap<>();
        sbTickets.forEach(sbTicket -> {
            String lineId = sbTicket.getLineId();
            String shiftNo = sbTicket.getShiftNo();

            Map<String, List<String>> ticketNoMap = pushTicketsBeans.computeIfAbsent(lineId, k -> new HashMap<>());

            List<String> ticketNoList = ticketNoMap.computeIfAbsent(shiftNo, k -> new ArrayList<>());

            ticketNoList.add(sbTicket.getTicketNo());
        });

        // 推送票据到pad
        pushTicketsBeans.forEach((lineId, ticketNoMap) -> {
            PushTicketsBean pushTicketsBean = new PushTicketsBean();

            pushTicketsBean.setLineNo(lineId);
            pushTicketsBean.setTicketNos(ticketNoMap);

            socketServerInnerApiClient.nofityNewTickets(pushTicketsBean);
        });

        log.info("********** 票据订单同步成功: {}", message);

        // 返回结果
        return ResponseMessage.success(message.getTransSn(), "ok");
    }
}
