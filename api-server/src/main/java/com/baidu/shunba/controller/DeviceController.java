package com.baidu.shunba.controller;

import com.baidu.shunba.bean.DeviceInitializationInformation;
import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.bean.ResponseMessage;
import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.bean.message.SecondDispatchRequestMessage;
import com.baidu.shunba.client.SocketServerInnerApiClient;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.entity.SBDevice;
import com.baidu.shunba.entity.SbDriverDispatchRecord;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.exceptions.BusinessHandlerException;
import com.baidu.shunba.service.BDSerialNumberService;
import com.baidu.shunba.service.SBDeviceService;
import com.baidu.shunba.queryvo.SBDeviceQueryVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("device")
@Slf4j
public class DeviceController {
    @Autowired
    private SBDeviceService sbDeviceService;

    @Autowired
    private SocketServerInnerApiClient socketServerInnerApiClient;

    @ApiOperation("获取设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isSearchDeleted", value = "是否查询已经被删除的设备, 默认为false不查询. 当请求中带有deleteFlag参数时, 忽略该参数")
    })
    @RequestMapping(value = "listDevice", method = RequestMethod.POST)
    public ResultVo<?> listDevice(@RequestBody QueryVo<SBDeviceQueryVO> queryVo, @RequestParam(name = "isSearchDeleted", defaultValue = "false", required = false) boolean isSearchDeleted) {
        try {
            return ResultVo.ok(sbDeviceService.commonQuery(queryVo, SBDeviceQueryVO.class, isSearchDeleted));
        } catch (IllegalAccessException | InstantiationException e) {
            return ResultVo.error("服务错误");
        }
    }

    /**
     * pad设备启动后, 同步后端以及设备的初始化信息. 设备传送当前版本参数到后端更新, 后端返回设备运行必须的参数
     *
     * @param deviceId   设备id
     * @param appVersion 设备当前应用version
     * @param osVersion  设备当前安卓系统version
     * @return DeviceInitializationInformation
     */
    @ApiOperation("pad设备启动后, 同步后端以及设备的初始化信息. 设备传送当前版本参数到后端更新, 后端返回设备运行必须的参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id."),
            @ApiImplicitParam(name = "appVersion", value = "设备当前应用version. 当同时传入了appVersion以及osVersion后, 更新设备的版本."),
            @ApiImplicitParam(name = "osVersion", value = "设备当前安卓系统version. 当同时传入了appVersion以及osVersion后, 更新设备的版本.")
    })
    @RequestMapping(value = "sync-init-info", method = RequestMethod.GET)
    public ResultVo<DeviceInitializationInformation> syncInitInfo(@RequestParam(name = "deviceId", required = true) String deviceId,
                                                                  @RequestParam(name = "appVersion", required = false) String appVersion,
                                                                  @RequestParam(name = "osVersion", required = false) String osVersion) {
        log.info("收到设备初始化数据: deviceId [{}], appVersion [{}], osVersion [{}]", deviceId, appVersion, osVersion);
        return ResultVo.ok(sbDeviceService.syncInitInfo(deviceId, appVersion, osVersion));
    }

    @ApiOperation("更新设备")
    @RequestMapping(value = "updateDevice", method = RequestMethod.POST)
    public ResultVo<SBDevice> updateDevice(@RequestBody SBDevice device) throws AppException {
        String carNo = device.getCarNo();
        String id = device.getId();

        SBDevice oldDevice = null;
        String oldCarNo = null;

        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(id)) {
            oldDevice = sbDeviceService.findById(id).orElse(null);
            if (null == oldDevice) {
                throw new AppException("指定设备不存在!", ResultEnum.DATA_VALIDATION_ERROR);
            }
            oldCarNo = oldDevice.getCarNo();
        }

        SBDevice sbDeviceSaved = sbDeviceService.save(device);

        if (oldDevice != null && !StringUtils.equals(carNo, oldCarNo)) {
            // 当传入的设备带有车牌号, 但是数据库存储的数据不带有车牌号, 或者车牌号不一致时, 发送初始化信号到设备
            socketServerInnerApiClient.sendInit(sbDeviceSaved.getDeviceId());
        }

        return ResultVo.ok(sbDeviceSaved);
    }

    @ApiOperation("删除设备")
    @RequestMapping(value = "deleteDevice", method = RequestMethod.DELETE)
    public ResultVo deleteDevice(String deviceId) throws AppException {
        if (StringUtils.isEmpty(deviceId)) {
            return ResultVo.error("设备ID为空");
        }

        SBDevice device = sbDeviceService.findByDeviceId(deviceId).orElse(null);

        if (device == null) {
            return ResultVo.error("设备不存在");
        }

        sbDeviceService.delete(device);
        socketServerInnerApiClient.pushAuth(device);
        return ResultVo.ok("已禁用");
    }

    @ApiOperation("顺巴推司机收发车")
    @RequestMapping(value = "receiveDispatch", method = RequestMethod.POST)
    public ResponseMessage receiveDispatch(@RequestBody SecondDispatchRequestMessage message) {
        SbDriverDispatchRecord sbDriverDispatchRecord;

        try {
            sbDriverDispatchRecord = sbDeviceService.receiveDispatch(message);
        } catch (BusinessHandlerException e) {
            return ResponseMessage.failure(message == null ? null : message.getTransSn(), e.getRetCode(), "falure", e.getRetMsg());
        }

        // 发送处理结果到socket
        socketServerInnerApiClient.sendDispatch(sbDriverDispatchRecord);

        log.info("********** 收发车处理完毕: {}", message);

        return ResponseMessage.success(message.getTransSn(), "ok");
    }
}
