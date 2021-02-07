package com.baidu.shunba.bean;

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

    @Override
    public String toString() {
        return this.getCode() + "";
    }

    public String getCodeAsString() {
        return this.code + "";
    }

    public static RetCode fromCode(int code) {
        for (RetCode value : RetCode.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        return RetCode.BUSINESS_ERROR;
    }

    public static RetCode fromStringCode(String code) {
        for (RetCode value : RetCode.values()) {
            if (value.getCodeAsString().equals(code)) {
                return value;
            }
        }

        return RetCode.BUSINESS_ERROR;
    }
}
