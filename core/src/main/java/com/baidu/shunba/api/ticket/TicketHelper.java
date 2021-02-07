package com.baidu.shunba.api.ticket;

import com.baidu.shunba.api.SecondUrl;
import com.baidu.shunba.api.ticket.bean.SecondTicket;
import com.baidu.shunba.api.ticket.bean.SecondTicketResult;
import com.baidu.shunba.api.ticket.bean.SecondTicketResultResponse;
import com.baidu.shunba.api.ticket.request.ReportSecondTicketResult;
import com.baidu.shunba.api.ticket.response.SecondTicketObject;
import com.baidu.shunba.api.ticket.response.SecondTicketResultObject;
import com.baidu.shunba.api.ticket.response.SecondTicketResultResponseObject;
import com.baidu.shunba.biz.entity.SBTicketEntity;
import com.baidu.shunba.common.gson.GsonUtils;
import com.baidu.shunba.common.utils.*;
import com.baidu.shunba.core.persistence.base.CriteriaQuery;
import com.baidu.shunba.core.persistence.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TicketHelper {
    private static final Logger logger = LoggerFactory.getLogger(TicketHelper.class);

    public static Collection<SBTicketEntity> processThirdTickets(SystemService systemService, SecondTicketObject ticketObject) {
        Date startDate = ticketObject.getStartDate();
        if (startDate == null) {
            startDate = new Date();
        }
        int startSeq = DateUtils.getYearMonthDaySeq(startDate);

        //1
        Map<String, SBTicketEntity> newTickets = new HashMap<String, SBTicketEntity>();
        for (SecondTicket ticket : ticketObject.ticketAry) {
            SBTicketEntity sbt = new SBTicketEntity();
            sbt.setSyncFlag(0);
            sbt.setTicketNo(ticket.ticketNo);
            sbt.setSeq(startSeq);

            //1
            if (StringUtils.isNotEmpty(ticket.memberId)) {
                sbt.setMemberId(ticket.memberId);
            }
            if (StringUtils.isNotEmpty(ticket.operate)) {
                sbt.setDelFlag(ObjectUtils.getIntValue(ticket.operate));
            }

            //3
            if (StringUtils.isNotEmpty(ticket.shiftNo)) {
                sbt.setShiftNo(ticket.shiftNo);
            }
            sbt.setLineId(ticket.lineId);

            newTickets.put(sbt.getTicketNo(), sbt);
        }

        //2
        CriteriaQuery cq = new CriteriaQuery(SBTicketEntity.class);
        cq.in("ticketNo", newTickets.keySet());
        cq.add();
        List<SBTicketEntity> tickets = systemService.getListByCriteriaQuery(cq, false);

        Map<String, SBTicketEntity> oldTickets = new HashMap<String, SBTicketEntity>();
        for (SBTicketEntity sbt : tickets) {
            oldTickets.put(sbt.getTicketNo(), sbt);
        }

        //3
        for (String ticketNo : newTickets.keySet()) {
            SBTicketEntity newT = newTickets.get(ticketNo);
            SBTicketEntity oldT = oldTickets.get(ticketNo);
            if (oldT == null) {
                oldTickets.put(ticketNo, newT);
            } else {
                if (ObjectUtils.getIntValue(oldT.getIsCheck()) == 1 && ObjectUtils.getIntValue(newT.getIsCheck()) != 1) {
                    systemService.addLog("服务器返回未核销票据，但是本地已核销:" + GsonUtils.toJson(oldT) + "," + GsonUtils.toJson(newT), 1, 1);
                }

                int syncFlag = ObjectUtils.getIntValue(oldT.getSyncFlag());
                try {
                    MyBeanUtils.copyBeanNotNull2Bean(newT, oldT);
                } catch (Exception e) {
                }
                if (syncFlag == 2) {
                    oldT.setSyncFlag(2);
                }
            }
        }

        systemService.batchSaveOrUpdate(oldTickets.values());

        return oldTickets.values();
    }



    public static Collection<SBTicketEntity> processThirdTicketsResult(SystemService systemService, SecondTicketResultObject ticketResultObject) {
        Date startDate = new Date();
        int startSeq = DateUtils.getYearMonthDaySeq(startDate);

        //1
        Map<String, SBTicketEntity> newTickets = new HashMap<String, SBTicketEntity>();
        for (SecondTicketResult ticket : ticketResultObject.ticketAry) {
            SBTicketEntity sbt = new SBTicketEntity();
            sbt.setSyncFlag(0);
            sbt.setTicketNo(ticket.ticketNo);
            sbt.setSeq(startSeq);

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

            if (StringUtils.isNotEmpty(ticket.getTimestamp())) {
                sbt.setCheckTime(ticket.getTimestamp());
            }

            newTickets.put(sbt.getTicketNo(), sbt);
        }

        //2
        CriteriaQuery cq = new CriteriaQuery(SBTicketEntity.class);
        cq.in("ticketNo", newTickets.keySet());
        cq.add();
        List<SBTicketEntity> tickets = systemService.getListByCriteriaQuery(cq, false);

        Map<String, SBTicketEntity> oldTickets = new HashMap<String, SBTicketEntity>();
        for (SBTicketEntity sbt : tickets) {
            oldTickets.put(sbt.getTicketNo(), sbt);
        }

        //3
        for (String ticketNo : newTickets.keySet()) {
            SBTicketEntity newT = newTickets.get(ticketNo);
            SBTicketEntity oldT = oldTickets.get(ticketNo);
            if (oldT == null) {
                oldTickets.put(ticketNo, newT);
            } else {
                if (ObjectUtils.getIntValue(oldT.getIsCheck()) == 1 && ObjectUtils.getIntValue(newT.getIsCheck()) != 1) {
                    systemService.addLog("服务器返回未核销票据，但是本地已核销:" + GsonUtils.toJson(oldT) + "," + GsonUtils.toJson(newT), 1, 1);
                }
                int syncFlag = ObjectUtils.getIntValue(oldT.getSyncFlag());
                try {
                    MyBeanUtils.copyBeanNotNull2Bean(newT, oldT);
                } catch (Exception e) {
                }
                if (syncFlag == 2) {
                    oldT.setSyncFlag(syncFlag);
                }
            }
        }

        systemService.batchSaveOrUpdate(oldTickets.values());

        return oldTickets.values();
    }


    public static void pushAndSyncTicketToSecondServer(SystemService systemService, Collection<SBTicketEntity> tickets) {
        try {
            ReportSecondTicketResult req = new ReportSecondTicketResult();
            req.transSn = SecondUrl.generateShortUuid();

            Map<String, SBTicketEntity> ticketMap = new HashMap<>();
            for (SBTicketEntity ticket : tickets) {
                if (ObjectUtils.getIntValue(ticket.getSyncFlag()) == 2) {
                    continue;
                }

                ticketMap.put(ticket.getTicketNo(), ticket);

                SecondTicketResult tTicket = new SecondTicketResult();
                tTicket.ticketNo = ticket.getTicketNo();
                tTicket.checkResult = ObjectUtils.getIntValue(ticket.getIsCheck()) == 1;
                tTicket.temperature = ObjectUtils.getDoubleValue(ticket.getTemperature());
                tTicket.checkType = ObjectUtils.getIntValue(ticket.getCheckType());
                tTicket.setTimestamp(ticket.getCheckTime());

                req.ticketAry.add(tTicket);
            }

            logger.info(" ======== 执行向顺吧上报核票数据: {}", req);
            String response = HttpUtils.fetchHttpUrlResponse(SecondUrl.TicketPush, HttpUtils.ContentType.JSON, GsonUtils.toJson(req));
            logger.info(" ======== 执行向顺吧上报核票数据成功, 核票结果: {}", response);

            SecondTicketResultResponseObject result = GsonUtils.fromJson(response, SecondTicketResultResponseObject.class);

            for (String ticketNo : result.ticketAry.keySet()) {
                SBTicketEntity ticket = ticketMap.get(ticketNo);
                if (ticket != null) {
                    SecondTicketResultResponse tRes = result.ticketAry.get(ticketNo);
                    ticket.setSyncFlag(tRes.success() ? 2 : 1);
                    ticket.setSyncMsg(tRes.msg);
                }
            }

            systemService.batchSaveOrUpdate(tickets);
        } catch (Exception e) {
            logger.error("TicketPush", e);
            e.printStackTrace();
        }
    }
}
