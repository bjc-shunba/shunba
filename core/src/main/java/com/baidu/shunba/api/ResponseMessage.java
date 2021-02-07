package com.baidu.shunba.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * 响应报文
 *
 * @param <T>
 */
@Data
public class ResponseMessage<T> {
    private static final String DEFAULT_RET_MESSAGE = "";

    private static final String BUSINESS_ERROR_MESSAGE = "业务处理失败";
    private static final String DB_ERROR_MESSAGE = "数据处理失败";

    /**
     * 交易流水号，全局唯一，长度10~30
     */
    private String transSn;

    /**
     * 返回码
     */
    @JsonSerialize(using = RetCodeSerializer.class)
    private RetCode retCode;

    /**
     * 响应数据(非必须)
     */
    private T data;

    /**
     * 返回信息(非必须)
     */
    private String retMsg = DEFAULT_RET_MESSAGE;

    private ResponseMessage(String transSn, RetCode retCode) {
        this.transSn = transSn;
        this.retCode = retCode;
    }

    private ResponseMessage(String transSn, RetCode retCode, String retMsg) {
        this.transSn = transSn;
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public ResponseMessage(String transSn, RetCode retCode, T data, String retMsg) {
        this.transSn = transSn;
        this.retCode = retCode;
        this.data = data;
        this.retMsg = retMsg;
    }

    /**
     * 处理成功
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param result  处理结果
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> success(String transSn, T result) {
        ResponseMessage<T> rm = new ResponseMessage<>(transSn, RetCode.SUCCESS);

        rm.data = result;

        return rm;
    }

    /**
     * 交易参数验证失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retMsg  返回信息
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> validError(String transSn, String retMsg) {
        return new ResponseMessage<>(transSn, RetCode.VALIDATE_ERROR, retMsg);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> businessError(String transSn) {
        return new ResponseMessage<>(transSn, RetCode.BUSINESS_ERROR, BUSINESS_ERROR_MESSAGE);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retMsg  返回信息
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> businessError(String transSn, String retMsg) {
        return new ResponseMessage<>(transSn, RetCode.BUSINESS_ERROR, retMsg);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> dbError(String transSn) {
        return new ResponseMessage<>(transSn, RetCode.DB_ERROR, DB_ERROR_MESSAGE);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retMsg  返回信息
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> dbError(String transSn, String retMsg) {
        return new ResponseMessage<>(transSn, RetCode.DB_ERROR, retMsg);
    }

    /**
     * 通用失败相应
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retCode 返回码
     * @param data    处理结果
     * @param retMsg  返回信息
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage<T> failure(String transSn, RetCode retCode, T data, String retMsg) {
        return new ResponseMessage<>(transSn, retCode, data, retMsg);
    }
}
