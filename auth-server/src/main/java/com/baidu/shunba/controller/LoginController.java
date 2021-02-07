package com.baidu.shunba.controller;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.service.LoginService;
import com.baidu.shunba.vo.LoginReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api("login")
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ApiOperation("用户登录")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResultVo login(@RequestBody LoginReq loginVO) throws IOException {
        return loginService.login(loginVO);
    }
}
