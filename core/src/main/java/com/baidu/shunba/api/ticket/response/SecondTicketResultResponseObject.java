package com.baidu.shunba.api.ticket.response;

import com.baidu.shunba.api.BaseBean;
import com.baidu.shunba.api.ticket.bean.SecondTicketResultResponse;

import java.util.Map;

public class SecondTicketResultResponseObject extends BaseBean {

    public int retCode;
    public String retMsg;

    public Map<String, SecondTicketResultResponse> ticketAry;
}
