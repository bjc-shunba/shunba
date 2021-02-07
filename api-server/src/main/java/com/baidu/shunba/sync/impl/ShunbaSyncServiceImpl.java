package com.baidu.shunba.sync.impl;

import com.baidu.shunba.bean.RetCode;
import com.baidu.shunba.entity.SBDriverClockRecord;
import com.baidu.shunba.entity.SBTicket;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.service.SBTicketService;
import com.baidu.shunba.sync.ShunbaSyncService;
import com.baidu.shunba.utils.DateUtils;
import com.baidu.shunba.utils.ObjectUtils;
import com.baidu.shunba.vo.PushTicketBean;
import com.baidu.shunba.vo.PushTicketRequest;
import com.baidu.shunba.vo.PushTicketResponse;
import com.baidu.shunba.vo.PushTicketResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class ShunbaSyncServiceImpl implements ShunbaSyncService {
    /**
     * 推送线程池
     */
    private final ExecutorService es = Executors.newFixedThreadPool(10);

    @Value("${shunba.driverClock}")
    private String driverClockUrl;

    @Value("${shunba.ticketPush}")
    private String ticketPush;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SBTicketService sbTicketService;

    @Override
    public void sendDriverClock(SBDriverClockRecord sbDriverClockRecord) {
        es.submit(() -> {
            log.info(" ======== 执行向顺吧上报司机打卡数据: " + new Gson().toJson(sbDriverClockRecord));

            JsonObject req = new JsonObject();
            req.addProperty("transSn", generateShortUuid());
            req.addProperty("transCode", "07");
            req.addProperty("remark", "");

            try {
                req.addProperty("driverId", Long.parseLong(sbDriverClockRecord.getDriverId()));
            } catch (Throwable e) {
                req.addProperty("driverId", sbDriverClockRecord.getDriverId());
            }

            req.addProperty("temperature", sbDriverClockRecord.getTemperature());
            req.addProperty("timestamp", DateUtils.getStringFromDate(sbDriverClockRecord.getClockTime()));

            try {
                ResponseEntity<String> res = restTemplate.postForEntity(driverClockUrl, req.toString(), String.class);
                log.info(" ======== 执行向顺吧上报司机打卡数据【" + new Gson().toJson(req) + "】成功, 返回响应码: {}, 请求结果: {}", res.getStatusCode().value(), res.getBody());
            } catch (RestClientException e) {
                log.error(" ======== 执行向顺吧上报司机打卡数据【" + new Gson().toJson(req) + "】失败: ", e);
            }
        });
    }

    @Override
    public void pushTicket(Collection<SBTicket> sbTickets) {
        es.submit(() -> {
            PushTicketRequest req = new PushTicketRequest(generateShortUuid());

            sbTickets.forEach(sbTicket -> {
                PushTicketBean bean = new PushTicketBean();

                bean.setCheckResult(ObjectUtils.sameWidth(sbTicket.getIsCheck(), 1));
                bean.setCheckType(ObjectUtils.getIntValue(sbTicket.getCheckType()));
                bean.setTemperature(ObjectUtils.getDoubleValue(sbTicket.getTemperature()));
                bean.setTicketNo(sbTicket.getTicketNo());
                bean.setTimestamp(DateUtils.getStringFromDate(sbTicket.getCheckTime()));

                req.addPushTicketBean(bean);
            });

            log.info(" ======== 执行向顺吧上报核票数据: {}", req);

            ResponseEntity<String> pushTicketResponseResponse = null;
            try {
                pushTicketResponseResponse = restTemplate.postForEntity(ticketPush, req, String.class);
            } catch (RestClientException e) {
                log.error(" ======== 执行向顺吧上报核票数据失败, transSn: {}, transCode: {}, 失败原因: {}", req.getTransSn(), req.getTransCode(), e);
                return;
            }

            String resBody = pushTicketResponseResponse.getBody();
            if (resBody == null) {
                log.error(" ======== 执行向顺吧上报核票数据失败, transSn: {}, transCode: {}, 失败结果: {}", req.getTransSn(), req.getTransCode(), pushTicketResponseResponse);
                return;
            }
            log.info(" ======== 执行向顺吧上报核票数据成功, 核票结果: {}", resBody);

            JsonObject asJsonObject = JsonParser.parseString(resBody).getAsJsonObject();

            PushTicketResponse pushTicketResponse = new Gson().fromJson(resBody, PushTicketResponse.class);
            try {
                pushTicketResponse.setRetCode(RetCode.fromStringCode(asJsonObject.get("retCode").getAsString()));
            } catch (Exception e) {
            }
            Map<String, PushTicketResult> ticketMap = pushTicketResponse.getTicketAry();
            List<SBTicket> forSave = new ArrayList<>();

            if (RetCode.SUCCESS == pushTicketResponse.getRetCode()) {
                // 处理成功, 保存核票结果
                sbTickets.forEach(sbTicket -> {
                    PushTicketResult ticketResult = ticketMap.get(sbTicket.getTicketNo());

                    if (ticketResult == null) {
                        return;
                    }
                    sbTicket.setSyncFlag(2);
                    sbTicket.setSyncMsg(ticketResult.getMsg());

                    forSave.add(sbTicket);
                });
            }

            if (forSave.isEmpty()) {
                return;
            }

            try {
                sbTicketService.saveAll(forSave);
                log.info(" ======== 保存顺吧核票同步结果成功, transSn: {}, transCode: {}", req.getTransSn(), req.getTransCode());
            } catch (AppException exception) {
                log.error(" ======== 保存顺吧核票同步结果失败, transSn: {}, transCode: {}, 失败原因: {}", req.getTransSn(), req.getTransCode(), exception);
            }
        });
    }

    private String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    private String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
