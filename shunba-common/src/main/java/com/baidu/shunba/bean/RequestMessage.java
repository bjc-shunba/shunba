package com.baidu.shunba.bean;

import lombok.Data;

/**
 * 请求报文
 */
@Data
public abstract class RequestMessage {
    /**
     * 交易流水号，全局唯一，长度10~30
     */
    private String transSn;

    /**
     * 交易代码，交易类型标志
     */
    private String transCode;
}
