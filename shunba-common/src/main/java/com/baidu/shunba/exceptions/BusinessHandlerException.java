package com.baidu.shunba.exceptions;

import com.baidu.shunba.bean.RetCode;

/**
 * 顺吧接口处理异常<br/>
 * 记录处理顺吧推送的任务处理时发生的异常
 */
public class BusinessHandlerException extends Exception {
    /**
     * 异常状态
     */
    private final RetCode retCode;

    /**
     * 错误原因
     */
    private final String retMsg;

    /**
     * 顺吧推送的交易码
     */
    private final String transSn;

    /**
     * constructor
     *
     * @param transSn 顺吧推送的交易码
     * @param retCode 异常状态
     * @param retMsg  错误原因
     */
    public BusinessHandlerException(String transSn, RetCode retCode, String retMsg) {
        super(retMsg);

        this.transSn = transSn;
        this.retMsg = retMsg;
        this.retCode = retCode;
    }

    public RetCode getRetCode() {
        return retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public String getTransSn() {
        return transSn;
    }
}
