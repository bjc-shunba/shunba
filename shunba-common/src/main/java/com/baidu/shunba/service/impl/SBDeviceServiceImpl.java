package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.DeviceInitializationInformation;
import com.baidu.shunba.bean.RetCode;
import com.baidu.shunba.bean.RuntimeConfigs;
import com.baidu.shunba.bean.message.SecondDispatchRequestMessage;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.dao.DeviceInitializationInformationRepository;
import com.baidu.shunba.dao.SBDeviceRepository;
import com.baidu.shunba.entity.*;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.exceptions.BusinessHandlerException;
import com.baidu.shunba.service.*;
import com.baidu.shunba.utils.DateUtils;
import com.baidu.shunba.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SBDeviceServiceImpl extends BaseServiceImpl<SBDevice, String> implements SBDeviceService {
    @Autowired
    private SBDeviceRepository sbDeviceRepository;

    @Autowired
    private DeviceInitializationInformationRepository deviceInitializationInformationRepository;

    @Autowired
    private SBAppVersionService sbAppVersionService;

    @Autowired
    private SBShiftService sbShiftService;

    @Autowired
    private SbDriverDispatchRecordService sbDriverDispatchRecordService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private BDSerialNumberService bdSerialNumberService;

    @Override
    protected JpaRepository<SBDevice, String> getJpaRepository() {
        return sbDeviceRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBDevice> getJpaSpecificationExecutor() {
        return sbDeviceRepository;
    }

    @Override
    public Optional<SBDevice> findByDeviceId(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }

        List<SBDevice> sbDevices = sbDeviceRepository.findAllByDeviceId(deviceId);

        return sbDevices == null || sbDevices.isEmpty() ? Optional.empty() : Optional.of(sbDevices.get(0));
    }

    @Override
    public SBDevice findByCarNo(String carNo) {
        if (StringUtils.isBlank(carNo)) {
            return null;
        }

        List<SBDevice> allByCarNo = sbDeviceRepository.findAllByCarNo(carNo);
        return allByCarNo == null || allByCarNo.isEmpty() ? null : allByCarNo.get(0);
    }

    @Override
    public DeviceInitializationInformation syncInitInfo(String deviceId, String appVersion, String osVersion) {
        // 获取设备基本参数
        DeviceInitializationInformation deviceInitializationInformation = deviceInitializationInformationRepository.findByDeviceId(deviceId);

        if (deviceInitializationInformation == null) {
            return null;
        }

        if (StringUtils.isNotBlank(appVersion) && StringUtils.isNotBlank(osVersion)) {
            log.info("执行同步设备版本: deviceId [{}], appVersion [{}], osVersion [{}]", deviceId, appVersion, osVersion);
            // 更新版本
            int i = sbDeviceRepository.updateVersions(deviceId, appVersion, osVersion);
            if (i == 1) {
                log.info("执行同步设备版本成功: deviceId [{}], appVersion [{}], osVersion [{}]", deviceId, appVersion, osVersion);
            }
        }

        // 获取最新版本
        SBAppVersion latestVersion = sbAppVersionService.getLatestVersion();

        if (latestVersion != null) {
            deviceInitializationInformation.setVersionDescription(latestVersion.getMemo());
            deviceInitializationInformation.setNewVersion(latestVersion.getVersion());
        }

        // 获取运行时参数
        RuntimeConfigs runtimeConfigs = sysConfigService.findAllRuntimeConfigs();

        if (runtimeConfigs != null) {
            deviceInitializationInformation.setDriverTemperatureCheck(runtimeConfigs.getDriverTemperatureCheck());
            deviceInitializationInformation.setFaceTimeout(runtimeConfigs.getFaceTimeout());
            deviceInitializationInformation.setPassengerTemperatureCheck(runtimeConfigs.getPassengerTemperatureCheck());
            deviceInitializationInformation.setQrcodeTimeout(runtimeConfigs.getQrcodeTimeout());
            deviceInitializationInformation.setTemperatureHigh(runtimeConfigs.getTemperatureHigh());
        }

        return deviceInitializationInformation;
    }

    @Override
    public SbDriverDispatchRecord receiveDispatch(SecondDispatchRequestMessage message) throws BusinessHandlerException {
        if (message == null) {
            throw new BusinessHandlerException(null, RetCode.VALIDATE_ERROR, "参数错误");
        }

        String transCode = message.getTransCode();
        String transSn = message.getTransSn();
        String shiftNo = message.getShiftNo();

        // 数据初步校验
        if (StringUtils.isBlank(transSn)) {
            throw new BusinessHandlerException(transSn, RetCode.VALIDATE_ERROR, "transSn不正确");
        }
        if (!"06".equals(transCode)) {
            throw new BusinessHandlerException(transSn, RetCode.VALIDATE_ERROR, "transCode不正确, 传递的transCode不是收发车信号06");
        }
        if (StringUtils.isEmpty(shiftNo)) {
            throw new BusinessHandlerException(transSn, RetCode.VALIDATE_ERROR, "车牌号不正确");
        }

        // 查找班次以及设备
        SBShift shift = sbShiftService.findByShiftNo(shiftNo);

        // 数据有效性校验
        if (shift == null) {
            throw new BusinessHandlerException(transSn, RetCode.BUSINESS_ERROR, "指定班次[" + shiftNo + "]不存在!");
        }
        String carNo = shift.getCarNo();
        if (StringUtils.isBlank(carNo)) {
            throw new BusinessHandlerException(transSn, RetCode.BUSINESS_ERROR, "指定班次[" + shiftNo + "]未配置关联车牌!");
        }
        List<SBDevice> deviceList = sbDeviceRepository.findAllByCarNo(carNo);
        if (deviceList.size() == 0) {
            throw new BusinessHandlerException(transSn, RetCode.BUSINESS_ERROR, "指定班次[" + shiftNo + "]配置的车牌号[" + carNo + "未配置关联设备]!");
        }
        if (deviceList.size() > 1) {
            throw new BusinessHandlerException(transSn, RetCode.BUSINESS_ERROR, "指定班次[" + shiftNo + "]配置的车牌号[" + carNo + "绑定了多个关联设备!");
        }
        SBDevice device = deviceList.get(0);
        if (StringUtils.isNotBlank(device.getShiftNo()) && message.getOperate() == 0) {
            throw new BusinessHandlerException(transSn, RetCode.BUSINESS_ERROR, "指定班次[" + shiftNo + "正处于发车状态, 无法重新发车!");
        }

        // 处理收发车
        if (message.getOperate() == 0) {
            device.setLineId(shift.getLineId());
            device.setShiftNo(shiftNo);
            device.setLastSeq(shift.getSeq());
        } else {
            device.setShiftNo(null);
            device.setLineId(null);
        }

        // 保存处理结果
        SbDriverDispatchRecord record = new SbDriverDispatchRecord();

        record.setDispatchTime(DateUtils.getDateFromString(message.getTimestamp()));
        record.setOperate(message.getOperate());
        record.setShiftNo(message.getShiftNo());
        record.setDeviceId(device.getDeviceId());

        try {
            sbDeviceRepository.saveAndFlush(device);
            return sbDriverDispatchRecordService.save(record);
        } catch (Throwable e) {
            log.error("保存收发车记录失败:", e);
            throw new BusinessHandlerException(transSn, RetCode.DB_ERROR, "数据操作错误!");
        }
    }

    /**
     * 更新设备序列号
     *
     * @param newSBDevice 新的设备配置
     * @param oldRelation 设备之前使用的序列号
     */
    private void updateBDSerialNumber(SBDevice newSBDevice, BDSerialNumber oldRelation) throws AppException {
        String newSerialNumberId = newSBDevice.getSerialNumberId();

        if (StringUtils.isNotBlank(newSerialNumberId)) {
            // 配置新序列号关联的设备为当前设备
            BDSerialNumber bySerialNumber = bdSerialNumberService.findById(newSerialNumberId).orElse(null);
            if (bySerialNumber != null) {
                if (ObjectUtils.sameWidth(bySerialNumber.getIsUsed(), 1)) {
                    throw new AppException("配置的新序列号已经被使用", ResultEnum.DATA_VALIDATION_ERROR);
                }
                bySerialNumber.setDeviceId(newSBDevice.getDeviceId());
                bdSerialNumberService.saveAndFlush(bySerialNumber);
            } else {
                throw new AppException("错误的序列号", ResultEnum.DATA_VALIDATION_ERROR);
            }
        }

        if (oldRelation != null) {
            // 清空旧序列号关联的设备
            oldRelation.setDeviceId(null);
            bdSerialNumberService.saveAndFlush(oldRelation);
        }
    }

    @Transactional
    @Override
    public SBDevice save(SBDevice entity) throws AppException {
        Date now = new Date();

        // 验证车牌以及设备id是否已被占用
        if (StringUtils.isNotBlank(entity.getCarNo())) {
            SBDevice allByCarNo = this.findByCarNo(entity.getCarNo());
            if (allByCarNo != null && !StringUtils.equalsIgnoreCase(entity.getId(), allByCarNo.getId())) {
                throw new AppException("该车牌已被占用!", ResultEnum.DATA_VALIDATION_ERROR);
            }
        }
        if (StringUtils.isNotBlank(entity.getDeviceId())) {
            SBDevice sbDevice = sbDeviceRepository.findByDeviceId(entity.getDeviceId()).orElse(null);
            if (sbDevice != null && !StringUtils.equalsIgnoreCase(entity.getId(), sbDevice.getId())) {
                throw new AppException("该设备已存在!", ResultEnum.DATA_VALIDATION_ERROR);
            }
        }

        // 新增
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateDate(now);
            entity.setUpdateDate(now);

            // 新增设备时, 取出可能已配置的序列号, 并进行关联
            BDSerialNumber bdSerialNumber = bdSerialNumberService.findByCarNo(entity.getCarNo());
            if (null == bdSerialNumber) {
                bdSerialNumber = bdSerialNumberService.findByDeviceId(entity.getDeviceId());
            }
            if (null != bdSerialNumber) {
                entity.setSerialNumberId(bdSerialNumber.getId());
            }

            return super.save(entity);
        }

        // 更新设备校验. 获取以存在的设备后对齐数据进行验证
        SBDevice existsDevice = sbDeviceRepository.findById(entity.getId()).orElse(null);
        if (null == existsDevice) {
            throw new AppException("指定设备不存在!", ResultEnum.DATA_VALIDATION_ERROR);
        }

        // 发车状态校验
        if (StringUtils.isNotBlank(existsDevice.getShiftNo()) && !StringUtils.equalsIgnoreCase(entity.getCarNo(), existsDevice.getCarNo())) {
            throw new AppException("处于发车状态的设备, 无法修改或者删除车牌", ResultEnum.DATA_VALIDATION_ERROR);
        }
        if (StringUtils.isNotBlank(existsDevice.getShiftNo()) && !StringUtils.equalsIgnoreCase(entity.getDeviceId(), existsDevice.getDeviceId())) {
            throw new AppException("处于发车状态的设备, 无法修改或者删除设备id", ResultEnum.DATA_VALIDATION_ERROR);
        }

        // 设备序列号校验
        BDSerialNumber serialNumber = existsDevice.getSerialNumber();

        if (serialNumber != null && ObjectUtils.sameWidth(serialNumber.getIsUsed(), 1) && StringUtils.isBlank(entity.getSerialNumberId())) {
            throw new AppException("已使用的序列号不能清除", ResultEnum.DATA_VALIDATION_ERROR);
        }

        // 如果设备新使用的序列号, 同之前使用的序列号不同, 更新序列号
        if (StringUtils.equalsIgnoreCase(entity.getSerialNumberId(), existsDevice.getSerialNumberId())) {
            updateBDSerialNumber(entity, serialNumber);
        }

        // 更新设备
        entity.setLineId(null);
        entity.setShiftNo(null);
        entity.setDelFlag(0);

        return super.save(entity);
    }

    @Override
    public void delete(SBDevice entity) throws AppException {
        entity.setDelFlag(1);

        // 更新关联的序列号, 将其关联的设备清空
        BDSerialNumber serialNumber = entity.getSerialNumber();
        if (serialNumber != null) {
            serialNumber.setDeviceId(null);
            serialNumber.setCarNo(null);

            bdSerialNumberService.save(serialNumber);
        }

        super.delete(entity);
    }

    @Override
    public void updateLastPostTicketTime(String deviceId) {
        sbDeviceRepository.updateLastPostTicketTime(deviceId, new Date());
    }
}
