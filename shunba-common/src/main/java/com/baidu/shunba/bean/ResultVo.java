package com.baidu.shunba.bean;

import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.exceptions.AppException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应结果
 *
 * @param <T>
 */
// @Deprecated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo<T> implements Serializable {
    /**
     * 消息码
     **/
    private int code;
    /**
     * 消息
     **/
    private String message;
    /**
     * 实体
     **/
    private T data;

    public static ResultVo ok() {
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    public static ResultVo ok(String msg) {
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(), msg, null);
    }

    public static <T> ResultVo<T> ok(T t) {
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), t);
    }

    public static ResultVo ok(ResultEnum codeEnum) {
        return new ResultVo<>(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static ResultVo ok(int code) {
        return new ResultVo<>(code, ResultEnum.SUCCESS.getMsg(), null);
    }

    public static ResultVo ok(int code, String msg) {
        return new ResultVo<>(code, msg, null);
    }

    public static <T> ResultVo<T> ok(int code, T data) {
        return new ResultVo<>(code, ResultEnum.SUCCESS.getMsg(), data);
    }

    public static <T> ResultVo<T> ok(ResultEnum codeEnum, T data) {
        return new ResultVo<>(codeEnum.getCode(), codeEnum.getMsg(), data);
    }

    public static <T> ResultVo<T> ok(int code, String msg, T data) {
        return new ResultVo<>(code, msg, data);
    }

    public static <T> ResultVo<T> error(T data) {
        return new ResultVo<>(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg(), data);
    }

    public static ResultVo error(Throwable e) {
        return new ResultVo<>(ResultEnum.UNKONW_ERROR.getCode(), e.getMessage(), e.getMessage());
    }

    public static <T> ResultVo<T> error(AppException e) {
        return new ResultVo(e.getResultEnum().getCode(), e.getMessage(), e.getMessage());
    }

    public static <T> ResultVo<T> error(Exception e, T data) {
        return new ResultVo<>(ResultEnum.UNKONW_ERROR.getCode(), e.getMessage(), data);
    }

    public static ResultVo error() {
        return new ResultVo<>(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg(), null);
    }

    public static ResultVo error(ResultEnum codeEnum) {
        return new ResultVo<>(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static <T> ResultVo<T> error(ResultEnum codeEnum, T data) {
        return new ResultVo<>(codeEnum.getCode(), codeEnum.getMsg(), data);
    }

    public static ResultVo error(String msg) {
        return error(ResultEnum.UNKONW_ERROR.getCode(), msg);
    }

    public static ResultVo error(int code, String msg) {
        return new ResultVo<>(code, msg, null);
    }

    public static <T> ResultVo<T> error(int code, String msg, T data) {
        return new ResultVo<>(code, msg, data);
    }
}
