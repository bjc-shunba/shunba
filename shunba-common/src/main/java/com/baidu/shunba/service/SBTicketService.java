package com.baidu.shunba.service;

import com.baidu.shunba.bean.message.TicketRequestMessage;
import com.baidu.shunba.entity.SBTicket;
import com.baidu.shunba.exceptions.AppException;

import java.util.Collection;
import java.util.List;

public interface SBTicketService extends BaseService<SBTicket, String> {
    List<SBTicket> findAllByLineId(String lineId);

    List<SBTicket> findAllBySeq(Integer seq);

    List<SBTicket> findAllByTicketNo(String ticketId);

    /**
     * 保存pad推送的核票结果
     *
     * @param tickets 核票结果
     * @return 保存核票结果后, 返回所有待同步的票据
     */
    List<SBTicket> saveTicketResult(List<SBTicket> tickets) throws AppException;

    /**
     * 处理顺吧推送的票据订单, 并返回持久化的订单数据
     *
     * @param message
     * @return
     */
    Collection<SBTicket> receiveTickets(TicketRequestMessage message) throws AppException;
}
