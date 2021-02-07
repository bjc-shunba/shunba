package com.baidu.shunba.bean;

import lombok.Data;

@Data
public class PullTicketsResponseBean {
    private String ticketNo;

    private boolean checkResult;

    private double temperature;

    private String checkType;

    private String timestamp;
}
