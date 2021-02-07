package com.baidu.shunba.constant;

/**
 * @ClassName ResultEnum
 * @Deacription 枚举维护, 将成功/异常统一在一个地方进行维护
 * @Author zsh
 * @Date 2019/12/23 15:34
 * @Version 1.0
 **/
public enum ResultEnum {

    /**
     * 未知错误
     */
    UNKONW_ERROR(-1, "未知错误"), SUCCESS(200, "成功"),

    /**
     * 登录状态码：1001-1999
     **/
    LOGIN_ERROR(1001, "系统繁忙，请稍后再试!"), LOGIN_CODE_ERROR(1002, "非法登录"),
    LOGIN_INFO_ERROR(1003, "用户名或密码错误!"),
    LOGIN_USERLOCK_ERROR(1004, "登录失败,此账号已经被锁定!"),
    LOGIN_EXPIRE_ERROR(1008, "登录失败,此账号已经过期!"),
    VERIFY_CODE_EXPIRE(1005, "验证码已经过期"),
    CODE_EMPTY(1006, "验证码不能为空"),
    OLD_PASSWORD_ERROR(1007, "原密码错误"),
    PARAM_IS_INVALID(20001, "参数无效"),
    PARAM_IS_BLANK(20002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(20003, "参数类型错误"), PARAM_NOT_COMPLETE(20004, "参数缺失"),
    NO_CORRESPONDING_TYPE(20005, "无对应类型"),
    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    ATTACHMENT_IS_EMPTY(40002, "附件信息不能为空"),
    /* 接口错误码：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限"),
    /**
     * 文件上传
     **/
    UPLOAD_SUCCESS(200, "上传成功"), UPLOAD_FAIL(1, "上传失败"),
    UPLOAD_TIME_OUT(2, "文件超时"), UPLOAD_FILE_OVERBIG(3, "文件过大"),
    UPLOAD_ISEMPTY(4, "文件获取失败"), UPLOAD_AUTHORIZATION_INSUFFICIENT(5, "授权码不足"),
    EXPORT_SUCCESS(5, "导出成功"),
    EXPORT_ERROR(6, "导出失败"), UPLOAD_USERCODE_EXIT(7, "用户编号已存在！"),
    UPLOAD_EMPTY(9, "数据为空!"), UPLOAD_USERNAME_EMPTY(9, "姓名为空！"),
    UPLOAD_USERCODE_EMPTY(9, "用户编号为空"), UPLOAD_USERSEX_EMPTY(9, "性别为空！"),
    UPLOAD_USERACCOUNT_EMPTY(9, "登录账号为空！"),

    /**
     * 资源类型
     **/
    INSERT_SUCCESS(200, "添加成功"),
    INSERT_ERROR(11, "该类型已存在"),

    /**
     * 业务异常
     **/
    DATA_VALIDATION_ERROR(10001, "数据验证错误"),
    HAS_NONE_UNUSED_VERSION_ERROR(10003, "当前不存在未下发的版本"),


    /**
     * db异常
     **/
    DB_EXCEPTION(10002, "系统繁忙，请稍后重试");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 枚举里面只要给get方法就可以了,因为枚举的使用都是直接用构造方法来创建,不会再从新set
     *
     * @return
     */
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}