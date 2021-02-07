package com.baidu.shunba.vo;

import lombok.Data;

@Data
public class PushTicketBean {
    private String ticketNo;

    private boolean checkResult;

    private double temperature;

    private int checkType;

    private String timestamp;

    private String remark = "";
}
