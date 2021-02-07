package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.*;
import com.baidu.shunba.bean.message.SecondDispatchRequestMessage;
import com.baidu.shunba.bean.message.TicketRequestMessage;
import com.baidu.shunba.bean.request.PullTicketsRequestMessage;
import com.baidu.shunba.bean.request.ShiftRequestMessage;
import com.baidu.shunba.bean.response.PullTicketsResponseMessage;
import com.baidu.shunba.client.*;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.entity.SBTicket;
import com.baidu.shunba.filter.impl.StringFilter;
import com.baidu.shunba.queryvo.SBTicketQueryVO;
import com.baidu.shunba.service.SBTicketService;
import com.baidu.shunba.service.ShunBaService;
import com.baidu.shunba.utils.DateUtils;
import com.baidu.shunba.utils.GsonUtils;
import com.baidu.shunba.vo.MemberObject;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShunBaServiceImpl implements ShunBaService {
    @Autowired
    private ApiServerClient apiServerClient;

    @Autowired
    private MemberApiClient memberApiClient;

    @Autowired
    private SBTicketService sbTicketService;

    @Override
    public String handlerShunBaRequest(String transSn, String transCode, String request) {
        log.info("========== {} ========== receive request, code: {}, message: {}", transSn, transCode, request);

        String result = null;
        try {
            switch (transCode) {
                // 线路班次信息推送
                case "01": {
                    result = receiveLineShift(transSn, request);
                    break;
                }
                // 票务订单推送
                case "02": {
                    result = receiveTickets(transSn, request);
                    break;
                }
                // 票务订单拉取, 已废弃
                // case "03": {}

                // 核票结果主动拉取
                case "05": {
                    result = queryTickets(transSn, request);
                    break;
                }
                // 司机收发车
                case "06": {
                    result = receiveDispatch(transSn, request);
                    break;
                }
                // 人脸数据下发
                case "08": {
                    result = uploadMemberFace(transSn, request);
                    break;
                }
                default:
                    result = ResponseMessage.validError(transSn, "不支持的transCode: " + transCode).asJsonString();
                    break;
            }
        } catch (Exception e) {
            log.error("========== {} ========== transCode: {}, 处理失败: {}", transSn, transCode, e);
            return ResponseMessage.businessError(transSn, "数据处理失败").asJsonString();
        }

        log.info("========== {} ========== transCode: {}, result : {}", transSn, transCode, result);

        return result;
    }

    /**
     * 线路班次信息推送<br />
     * /ticket-server/api/shift/receiveLineShift
     *
     * @param transSn 交易码
     * @param request 请求json
     * @return 处理结果json
     */
    private String receiveLineShift(String transSn, String request) {
        try {
            ShiftRequestMessage shiftRequestMessage = new Gson().fromJson(request, ShiftRequestMessage.class);

            return apiServerClient.receiveLineShift(shiftRequestMessage);
        } catch (JsonSyntaxException e) {
            return ResponseMessage.validError(transSn, "参数验证失败").asJsonString();
        }
    }

    /**
     * 票务订单推送<br />
     * /ticket-server/api/ticket/receiveTickets
     *
     * @param transSn 交易码
     * @param request 请求json
     * @return 处理结果json
     */
    private String receiveTickets(String transSn, String request) {
        try {
            TicketRequestMessage ticketRequestMessage = new Gson().fromJson(request, TicketRequestMessage.class);

            return apiServerClient.receiveTickets(ticketRequestMessage);
        } catch (JsonSyntaxException e) {
            return ResponseMessage.validError(transSn, "参数验证失败").asJsonString();
        }
    }

    /**
     * 核票结果主动拉取<br />
     *
     * @param transSn 交易码
     * @param request 请求json
     * @return 处理结果json
     */
    private String queryTickets(String transSn, String request) {
        PullTicketsRequestMessage dispatchRequestMessage = null;

        try {
            dispatchRequestMessage = new Gson().fromJson(request, PullTicketsRequestMessage.class);
        } catch (JsonSyntaxException e) {
            return ResponseMessage.validError(transSn, "参数验证失败").asJsonString();
        }

        if (dispatchRequestMessage == null || StringUtils.isBlank(dispatchRequestMessage.getShiftNo())) {
            return ResponseMessage.validError(transSn, "参数验证失败").asJsonString();
        }

        SBTicketQueryVO queryCriteria = new SBTicketQueryVO();

        StringFilter shiftId = new StringFilter();
        shiftId.setEquals(dispatchRequestMessage.getShiftNo());
        queryCriteria.setShiftId(shiftId);

        List<SBTicket> list = sbTicketService.findAll(queryCriteria);

        List<PullTicketsResponseBean> results = list.stream()
                .map(ticket -> {
                    PullTicketsResponseBean bean = new PullTicketsResponseBean();

                    bean.setCheckResult(ticket.getIsCheck() == 1);
                    bean.setCheckType(ticket.getCheckType() + "");
                    bean.setTemperature(ticket.getTemperature());
                    bean.setTicketNo(ticket.getTicketNo());
                    bean.setTimestamp(DateUtils.getStringFromDate(ticket.getCheckTime()));

                    return bean;
                })
                .collect(Collectors.toList());

        return new PullTicketsResponseMessage(transSn, RetCode.SUCCESS, results).asJsonString();
    }

    /**
     * 司机收发车<br />
     *
     * @param transSn 交易码
     * @param request 请求json
     * @return 处理结果json
     */
    private String receiveDispatch(String transSn, String request) {
        try {
            SecondDispatchRequestMessage dispatchRequestMessage = new Gson().fromJson(request, SecondDispatchRequestMessage.class);

            return apiServerClient.receiveDispatch(dispatchRequestMessage);
        } catch (JsonSyntaxException e) {
            return ResponseMessage.validError(transSn, "参数验证失败").asJsonString();
        }
    }

    /**
     * 人脸数据下发<br />
     * /api-server/member/uploadMemberFace
     *
     * @param transSn 交易码
     * @param request 请求json
     * @return 处理结果json
     */
    private String uploadMemberFace(String transSn, String request) {
        String resultJson = null;

        try {
            MemberObject memberObject = new Gson().fromJson(request, MemberObject.class);

            resultJson = memberApiClient.uploadMemberFace(memberObject);
        } catch (JsonSyntaxException e) {
            return ResponseMessage.validError(transSn, "参数验证失败").asJsonString();
        }

        JsonObject result = new JsonParser().parse(resultJson).getAsJsonObject();
        int code = GsonUtils.getIntValue(result, "code", ResultEnum.UNKONW_ERROR.getCode());

        if (code == ResultEnum.SUCCESS.getCode()) {
            return ResponseMessage.success(transSn, "ok").asJsonString();
        } else {
            return ResponseMessage.businessError(transSn, GsonUtils.getStringValue(result, "message")).asJsonString();
        }
    }
}
