package com.baidu.shunba.bean;

import com.baidu.shunba.jackson.deserializer.RetCodeJsonDeserializer;
import com.baidu.shunba.jackson.serializer.RetCodeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 响应报文
 */
@Data
public abstract class ResponseMessage {
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
    @JsonDeserialize(using = RetCodeJsonDeserializer.class)
    private RetCode retCode;

    /**
     * 返回信息(非必须)
     */
    private String retMsg = DEFAULT_RET_MESSAGE;

    public ResponseMessage() {
    }

    protected ResponseMessage(String transSn, RetCode retCode) {
        this.transSn = transSn;
        this.retCode = retCode;
    }

    protected ResponseMessage(String transSn, RetCode retCode, String retMsg) {
        this.transSn = transSn;
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    /**
     * 转换结果为json
     *
     * @return json string
     */
    public String asJsonString() {
        Gson gson = new Gson();

        JsonObject object = gson.toJsonTree(this).getAsJsonObject();

        object.addProperty("retCode", this.retCode.getCode());

        return object.toString();
    }

    /**
     * 处理成功
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param result  处理结果
     * @param <T>     处理结果类型
     * @return ResponseMessage
     */
    public static <T> ResponseMessage success(String transSn, T result) {
        return new CommonResponseMessage<>(transSn, RetCode.SUCCESS, result);
    }

    /**
     * 交易参数验证失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retMsg  返回信息
     * @return ResponseMessage
     */
    public static ResponseMessage validError(String transSn, String retMsg) {
        return new CommonResponseMessage<>(transSn, RetCode.VALIDATE_ERROR, retMsg);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @return ResponseMessage
     */
    public static ResponseMessage businessError(String transSn) {
        return new CommonResponseMessage<>(transSn, RetCode.BUSINESS_ERROR, BUSINESS_ERROR_MESSAGE);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retMsg  返回信息
     * @return ResponseMessage
     */
    public static ResponseMessage businessError(String transSn, String retMsg) {
        return new CommonResponseMessage<>(transSn, RetCode.BUSINESS_ERROR, retMsg);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @return ResponseMessage
     */
    public static ResponseMessage dbError(String transSn) {
        return new CommonResponseMessage<>(transSn, RetCode.DB_ERROR, DB_ERROR_MESSAGE);
    }

    /**
     * 业务处理失败
     *
     * @param transSn 交易流水号，全局唯一，长度10~30
     * @param retMsg  返回信息
     * @return ResponseMessage
     */
    public static ResponseMessage dbError(String transSn, String retMsg) {
        return new CommonResponseMessage<>(transSn, RetCode.DB_ERROR, retMsg);
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
    public static <T> ResponseMessage failure(String transSn, RetCode retCode, T data, String retMsg) {
        return new CommonResponseMessage<>(transSn, retCode, retMsg, data);
    }

    /**
     * 通用响应报文
     *
     * @param <T>
     */
    private static class CommonResponseMessage<T> extends ResponseMessage {
        /**
         * 响应数据(非必须)
         */
        @Getter
        @Setter
        private T data;

        private CommonResponseMessage(String transSn, RetCode retCode, String retMsg) {
            super(transSn, retCode, retMsg);
        }

        private CommonResponseMessage(String transSn, RetCode retCode, T data) {
            super(transSn, retCode);
            this.data = data;
        }

        private CommonResponseMessage(String transSn, RetCode retCode, String retMsg, T data) {
            super(transSn, retCode, retMsg);
            this.data = data;
        }
    }
}
