package com.baidu.shunba.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PushTicketsBean {
    private String lineNo;

    private Map<String, List<String>> ticketNos;
}
