package com.baidu.shunba.exceptions;

import com.baidu.shunba.constant.ResultEnum;

/**
 * 应用异常
 */
public class AppException extends Exception {
    private final ResultEnum resultEnum;

    public AppException(String message) {
        this(message, ResultEnum.UNKONW_ERROR);
    }

    public AppException(String message, ResultEnum resultEnum) {
        super(message);
        this.resultEnum = resultEnum;
    }

    public ResultEnum getResultEnum() {
        return resultEnum;
    }
}
