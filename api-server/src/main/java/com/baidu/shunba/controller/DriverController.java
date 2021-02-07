package com.baidu.shunba.controller;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.entity.SBDriverClockRecord;
import com.baidu.shunba.service.SBDriverClockRecordService;
import com.baidu.shunba.sync.ShunbaSyncService;
import com.baidu.shunba.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api("司机相关服务")
@RestController
@RequestMapping("driver")
@Slf4j
public class DriverController {
    @Autowired
    private SBDriverClockRecordService sbDriverClockRecordService;

    @Autowired
    private ShunbaSyncService shunbaSyncService;

    @ApiOperation("司机打卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备ID"),
            @ApiImplicitParam(name = "driverId", value = "司机ID"),
            @ApiImplicitParam(name = "temperature", value = "司机当前温度"),
            @ApiImplicitParam(name = "timestamp", value = "上报时间")
    })
    @RequestMapping(path = "clock", method = {RequestMethod.GET})
    public ResultVo<String> driverClock(@RequestParam(name = "deviceId", required = true) String deviceId,
                                        @RequestParam(name = "driverId", required = true) String driverId,
                                        @RequestParam(name = "temperature", required = true) double temperature,
                                        @RequestParam(name = "timestamp", required = true) String timestamp) {
        SBDriverClockRecord record = new SBDriverClockRecord();

        Date clockTime = DateUtils.getDateFromString(timestamp);

        record.setClockTime(clockTime);
        record.setCreateDate(new Date());
        record.setDeviceId(deviceId);
        record.setDriverId(driverId);
        record.setTemperature(temperature);

        SBDriverClockRecord sbDriverClockRecord = sbDriverClockRecordService.save(record);

        log.info("司机【" + new Gson().toJson(sbDriverClockRecord) + "】打卡成功!");

        // 上报司机打卡信息
        shunbaSyncService.sendDriverClock(sbDriverClockRecord);

        return ResultVo.ok("OK");
    }
}
