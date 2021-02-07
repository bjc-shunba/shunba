package com.baidu.shunba.bean.message;

import com.baidu.shunba.bean.RequestMessage;
import lombok.Data;

import java.util.List;

@Data
public class TicketRequestMessage extends RequestMessage {
    List<SecondTicket> ticketAry;
}
