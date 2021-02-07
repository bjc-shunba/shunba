package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.constant.BcryptUtil;
import com.baidu.shunba.constant.Constant;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.dao.UserRepository;
import com.baidu.shunba.entity.User;
import com.baidu.shunba.service.LoginService;
import com.baidu.shunba.vo.LoginInfoResp;
import com.baidu.shunba.vo.LoginReq;
import com.baidu.shunba.vo.TokenVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResultVo<LoginInfoResp> login(LoginReq loginReq) throws IOException {
        if (StringUtils.isEmpty(loginReq.getUsername()) || StringUtils.isEmpty(loginReq.getPassword())) {
            return ResultVo.error(ResultEnum.LOGIN_INFO_ERROR);
        }

        User user = userRepository.findByUserNameAndAndDeleted(loginReq.getUsername(), "N");

        if (user == null) {
            return ResultVo.error(ResultEnum.LOGIN_INFO_ERROR);
        }

//        if (!user.getPassword().equals(loginReq.getPassword())) {
//            return ResultVo.error(ResultEnum.LOGIN_INFO_ERROR);
//        }

        Response oAuthresponse = postToOauth(loginReq);

        if (oAuthresponse == null || oAuthresponse.code() != HttpStatus.OK.value()) {
            return ResultVo.error(ResultEnum.PERMISSION_NO_ACCESS);
        }

        log.info("OAuth服务器获取Token成功");
        String result = oAuthresponse.body().string();
        // 登录认证通过后查询该用户密码是否为初始密码, 返回标记, 前端通过标记判断是否需修改密码
        ObjectMapper objectMapper = new ObjectMapper();
        TokenVO tokenVO = objectMapper.readValue(result, TokenVO.class);

        LoginInfoResp loginInfoResp = new LoginInfoResp();

        loginInfoResp.setTokenVO(tokenVO);
        loginInfoResp.setId(user.getId());

        return ResultVo.ok(ResultEnum.SUCCESS, loginInfoResp);
    }

    public Response postToOauth(LoginReq loginReq) throws IOException {
        OkHttpClient okHttpClient =
                new OkHttpClient.Builder().connectTimeout(Constant.NUM_TEN, TimeUnit.SECONDS)
                        .writeTimeout(Constant.NUM_TEN, TimeUnit.SECONDS)
                        .readTimeout(Constant.NUM_TWENTY, TimeUnit.SECONDS).build();
        // post方式提交的数据
        FormBody formBody = new FormBody.Builder().add(Constant.GRANT_TYPE_KEY, Constant.GRANT_TYPE_VALUE)
                .add(Constant.CLIENT_ID_KEY, Constant.CLIENT_ID_VALUE)
                .add(Constant.CLIENT_SECRET_KEY, Constant.FINAL_SECRET)
                .add(Constant.USER_NAME, loginReq.getUsername())
                .add(Constant.GRANT_TYPE_VALUE, passwordEncoder.encode(loginReq.getPassword())).build();

        // 请求的url
        final Request request1 = new Request.Builder().url(Constant.SECUTITY_URL).post(formBody).build();
        return okHttpClient.newCall(request1).execute();
    }
}
