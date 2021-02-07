package com.baidu.shunba.server.inner;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baidu.shunba.biz.bean.socket.PushTickets;
import com.baidu.shunba.biz.entity.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.baidu.shunba.common.utils.DateUtils;
import com.baidu.shunba.common.utils.ObjectUtils;
import com.baidu.shunba.common.utils.SUSequenceUtils;
import com.baidu.shunba.common.utils.StringUtils;
import com.baidu.shunba.core.config.SysConfigEntity;
import com.baidu.shunba.core.persistence.base.CriteriaQuery;
import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.core.web.bean.MessageJson;
import com.baidu.shunba.socket.bean.ResponseBean;
import com.baidu.shunba.socket.bean.SocketAction;
import com.baidu.shunba.socket.bean.resp.NewTicketsResponse;
import com.baidu.shunba.socket.service.ServerHelper;

@Scope("prototype")
@Controller
@RequestMapping("/api/inner")
public class InnerApi {
    private static final Logger logger = LoggerFactory.getLogger(InnerApi.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping("/nofityNewTicket")
    @ResponseBody
    public MessageJson nofityNewTicket(@RequestBody SBTicketEntity ticket) {
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.newTicket);
        message.setContent(ticket);
        if (StringUtils.isNotEmpty(ticket.getShiftNo())) {
            return ServerHelper.sendMessageToShift(systemService, ticket.getShiftNo(), message);
        } else if (StringUtils.isNotEmpty(ticket.getLineId())) {
            return ServerHelper.sendMessageToLine(systemService, ticket.getLineId(), message);
        }
        return MessageJson.newInstance("ok");
    }

    @RequestMapping("/nofityNewVerion")
    @ResponseBody
    public MessageJson nofityNewTicket(@RequestBody SBAppVersionEntity appVer) {
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.newVersion);
        message.setContent(appVer);
        return ServerHelper.sendMessageToAll(systemService, message);
    }

    /**
     * 向指定设备发送初始化信号
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/init", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("向指定设备发送初始化信号")
    @ResponseBody
    public MessageJson init(@RequestParam(name = "deviceId") String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return MessageJson.newInstanceWithError("参数错误");
        }

        return ServerHelper.sendInit(deviceId);
    }

    /**
     * 发送初始化信号到所有设备
     *
     * @return
     */
    @RequestMapping(value = "/init-all", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("发送初始化信号到所有设备")
    @ResponseBody
    public MessageJson init() {
        return ServerHelper.sendInitAll();
    }

    @RequestMapping("/nofityNewTickets")
    @ResponseBody
    public MessageJson nofityNewTickets(@RequestBody PushTickets tickets) {
        //TODO, 看看顺巴数据，线路与班次的ID是唯一的还是复用。班次和线路是不是必填
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.newTickets);
        for (Map.Entry<String, List<String>> entity : tickets.ticketNos.entrySet()) {
            NewTicketsResponse content = new NewTicketsResponse();
            content.ticketNos = entity.getValue();
            message.setContent(content);

            ServerHelper.sendMessageToShift(systemService, entity.getKey(), message);
        }
        return MessageJson.newInstance("OK");
    }

    @RequestMapping("/pushAuth")
    @ResponseBody
    public MessageJson pushAuth(@RequestBody SBDeviceEntity device) {
        if (ObjectUtils.getIntValue(device.getDelFlag()) == 1) {
            return ServerHelper.pushInvalidToDevice(systemService, device.getDeviceId());
        } else {
            return ServerHelper.pushAuthToDevice(systemService, device);
        }
    }

    @RequestMapping("/sendDispatch")
    @ResponseBody
    public MessageJson pushConfig(@RequestBody SbDriverDispatchRecord record) {
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.sendDispatch);
        message.setContent(record.getOperate());
        return ServerHelper.sendMessageToAll(systemService, message);
    }

    @RequestMapping("/pushConfig")
    @ResponseBody
    public MessageJson pushConfig(@RequestBody List<SysConfigEntity> configs) {
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.pushAuth);
        message.setContent(configs);
        return ServerHelper.sendMessageToAll(systemService, message);
    }


    @RequestMapping("/call4UploadTickets")
    @ResponseBody
    public MessageJson call4UploadTickets(String deviceId) {
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.uploadTickets);
        return ServerHelper.sendMessageToOne(systemService, deviceId, message);
    }


    @RequestMapping("/nofityNewFace")
    @ResponseBody
    public MessageJson nofityNewFace(@RequestBody SBMemberEntity member) {
        ResponseBean message = new ResponseBean(SUSequenceUtils.generateNumberSequence(), SocketAction.newFace);
        logger.debug(" ******** 收到票务订单: " + member);
        message.setContent(member);

        int seq = DateUtils.getYearMonthDaySeq(new Date());
        CriteriaQuery cq = new CriteriaQuery(SBTicketEntity.class);
        cq.eq("seq", seq);
        cq.eq("memberId", member.getMemberId());
        cq.add();

        List<SBTicketEntity> tickets = systemService.getListByCriteriaQuery(cq, false);

        if (tickets != null && !tickets.isEmpty()) {
            for (SBTicketEntity ticket : tickets) {
                if (StringUtils.isNotEmpty(ticket.getShiftNo())) {
                    logger.debug(" ******** 发送票据[" + member + "]到指定班次: " + ticket.getShiftNo());
                    ServerHelper.sendMessageToLine(systemService, ticket.getShiftNo(), message);
                } else if (StringUtils.isNotEmpty(ticket.getLineId())) {
                    logger.debug(" ******** 发送票据[" + member + "]到指定线路: " + ticket.getShiftNo());
                    ServerHelper.sendMessageToShift(systemService, ticket.getLineId(), message);
                }
            }
        } else {
            logger.debug(" ******** 发送票据[" + member + "]到全部设备: ");
            ServerHelper.sendMessageToAll(systemService, message);
        }

        return MessageJson.newInstance("ok");
    }


}
