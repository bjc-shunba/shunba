package com.baidu.shunba.api.ticket.bean;

public class SecondTicketResultResponse {

    private int ret;
    public String msg;

    public boolean success() {
        return ret == 0;
    }
}
