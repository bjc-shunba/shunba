package com.baidu.shunba.api;

import lombok.Getter;

public enum RetCode {
    /**
     * 交易成功
     */
    SUCCESS(0),
    /**
     * 收单验证错误
     */
    VALIDATE_ERROR(1),
    /**
     * 交易错误
     */
    BUSINESS_ERROR(2),
    /**
     * 数据库操作失败
     */
    DB_ERROR(3);

    @Getter
    private final int code;

    RetCode(int code) {
        this.code = code;
    }
}
