package com.baidu.shunba.service;

import com.baidu.shunba.bean.ResponseMessage;
import com.google.gson.JsonObject;

/**
 * 处理顺吧请求
 */
public interface ShunBaService {
    String handlerShunBaRequest(String transSn, String transCode, String request);
}
