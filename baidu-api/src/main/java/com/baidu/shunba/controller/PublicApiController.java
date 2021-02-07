package com.baidu.shunba.controller;

import com.baidu.shunba.bean.ResponseMessage;
import com.baidu.shunba.service.ShunBaService;
import com.baidu.shunba.utils.GsonUtils;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api
@RestController
@RequestMapping("api")
@Slf4j
public class PublicApiController {
    @Autowired
    private ShunBaService shunBaService;

    @ApiOperation("顺吧公共api")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String handlerShunBaRequest(@RequestBody String body) {
        JsonObject request = new JsonParser().parse(body).getAsJsonObject();

        String transSn = GsonUtils.getStringValue(request, "transSn");
        String transCode = GsonUtils.getStringValue(request, "transCode");

        if (StringUtils.isBlank(transSn)) {
            return ResponseMessage.validError(transSn, "transSn不能为空").asJsonString();
        }

        if (StringUtils.isBlank(transCode)) {
            return ResponseMessage.validError(transSn, "transCode不能为空").asJsonString();
        }

        return shunBaService.handlerShunBaRequest(transSn, transCode, body);
    }
}
