package com.baidu.shunba.service;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.vo.LoginInfoResp;
import com.baidu.shunba.vo.LoginReq;

import java.io.IOException;

public interface LoginService {
    ResultVo<LoginInfoResp> login(LoginReq loginReq) throws IOException;
}
