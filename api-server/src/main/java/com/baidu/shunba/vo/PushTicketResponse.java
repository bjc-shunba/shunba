package com.baidu.shunba.vo;

import com.baidu.shunba.bean.ResponseMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class PushTicketResponse extends ResponseMessage {
    @Getter
    @Setter
    private Map<String, PushTicketResult> ticketAry;
}
