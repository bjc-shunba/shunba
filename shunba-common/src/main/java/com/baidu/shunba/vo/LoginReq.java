package com.baidu.shunba.vo;

import lombok.Data;

@Data
public class LoginReq {
    private String username;
    private String password;
    private String tk;
    private String ds;
}
