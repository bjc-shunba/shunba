package com.baidu.shunba.vo;

import lombok.Data;

@Data
public class LoginInfoResp {
    /**
     * 是否需要更新密码
     */
    private Boolean updateFlag;

    /**
     * 登陆认证token
     */
    private TokenVO tokenVO;

    /**
     * 用户Id
     */
    private String id;
}
