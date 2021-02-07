package com.baidu.shunba.vo;

import com.baidu.shunba.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestPushThirdTicketResult {

    public String transSn;
    public String transCode;
    private String transitDate;

    public void setTransitDate(Date date) {
        transitDate = DateUtils.getDateStringWithFormat(date, "yyyy-MM-dd");
    }

    public final List<ThirdTicket> ticketAry = new ArrayList<>();
}
