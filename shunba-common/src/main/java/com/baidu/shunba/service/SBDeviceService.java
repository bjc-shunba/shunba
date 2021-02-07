package com.baidu.shunba.service;

import com.baidu.shunba.bean.DeviceInitializationInformation;
import com.baidu.shunba.bean.message.SecondDispatchRequestMessage;
import com.baidu.shunba.entity.SBDevice;
import com.baidu.shunba.entity.SbDriverDispatchRecord;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.exceptions.BusinessHandlerException;

import java.util.Optional;

public interface SBDeviceService extends BaseService<SBDevice, String> {
    /**
     * 根据deviceId, 查找设备
     *
     * @param deviceId device_id
     * @return SBDevice
     */
    Optional<SBDevice> findByDeviceId(String deviceId);

    /**
     * 根据汽车车牌查找汽车
     *
     * @param carNo
     * @return
     */
    SBDevice findByCarNo(String carNo);

    /**
     * 同步设备版本号到指定设备, 并返回设备运行参数
     *
     * @param deviceId   设备id
     * @param appVersion 设备当前应用version
     * @param osVersion  设备当前安卓系统version
     * @return 设备运行参数
     */
    DeviceInitializationInformation syncInitInfo(String deviceId, String appVersion, String osVersion);

    /**
     * 顺巴推司机收发车
     *
     * @param message 推送参数
     * @return 收发车记录
     * @throws BusinessHandlerException 处理错误时, 返回处理错误原因
     */
    SbDriverDispatchRecord receiveDispatch(SecondDispatchRequestMessage message) throws BusinessHandlerException;

    @Override
    void delete(SBDevice entity) throws AppException;

    /**
     * 更新设备最近一次提交票据的时间
     * @param deviceId
     */
    void updateLastPostTicketTime(String deviceId);
}
