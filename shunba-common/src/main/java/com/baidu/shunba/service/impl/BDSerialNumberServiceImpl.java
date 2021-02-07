package com.baidu.shunba.service.impl;

import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.dao.BDSerialNumberRepository;
import com.baidu.shunba.entity.BDSerialNumber;
import com.baidu.shunba.entity.SBDevice;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.service.BDSerialNumberService;
import com.baidu.shunba.service.SBDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class BDSerialNumberServiceImpl extends BaseServiceImpl<BDSerialNumber, String> implements BDSerialNumberService {
    @Autowired
    private BDSerialNumberRepository bdSerialNumberRepository;

    @Autowired
    private SBDeviceService sbDeviceService;

    @Override
    protected JpaRepository<BDSerialNumber, String> getJpaRepository() {
        return bdSerialNumberRepository;
    }

    @Override
    protected JpaSpecificationExecutor<BDSerialNumber> getJpaSpecificationExecutor() {
        return bdSerialNumberRepository;
    }

    @Override
    public BDSerialNumber findBySerialNumber(String serialNumber) {
        if (StringUtils.isBlank(serialNumber)) {
            return null;
        }

        List<BDSerialNumber> serialNumbers = bdSerialNumberRepository.findBySerialNumber(serialNumber);

        return serialNumbers == null || serialNumbers.isEmpty() ? null : serialNumbers.get(0);
    }

    @Override
    public BDSerialNumber findByCarNo(String carNo) {
        if (StringUtils.isBlank(carNo)) {
            return null;
        }

        List<BDSerialNumber> serialNumbers = bdSerialNumberRepository.findByCarNo(carNo);

        return serialNumbers == null || serialNumbers.isEmpty() ? null : serialNumbers.get(0);
    }

    @Override
    public BDSerialNumber findByDeviceId(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }

        List<BDSerialNumber> serialNumbers = bdSerialNumberRepository.findByDeviceId(deviceId);

        return serialNumbers == null || serialNumbers.isEmpty() ? null : serialNumbers.get(0);
    }

    @Transactional
    @Override
    public BDSerialNumber saveAndFlush(BDSerialNumber entity) throws AppException {
        /**
         * 更新序列号时, 保证:
         * <ul>
         *     <li>1. 序列号必填且未被使用</li>
         *     <li>2. 设备标识码未被占用</li>
         *     <li>3. 车牌号未被占用</li>
         *     <li>4. 设备标识码、车牌号至少有一个填写, 且以车牌号优先</li>
         *     <li>5. 发车状态不准修改</li>
         *     <li>6. 删除序列号时不用做2 3校验</li>
         * </ul>
         */
        Date now = new Date();

        if (StringUtils.isBlank(entity.getSerialNumber())) {
            throw new AppException("参数错误, 请填写serialNumber", ResultEnum.DATA_VALIDATION_ERROR);
        }

        // 数据存在校验
        BDSerialNumber bdSerialNumber = this.findBySerialNumber(entity.getSerialNumber());
        if (bdSerialNumber != null && !StringUtils.equalsIgnoreCase(entity.getId(), bdSerialNumber.getId())) {
            throw new AppException("序列号已被占用", ResultEnum.DATA_VALIDATION_ERROR);
        }
        bdSerialNumber = this.findByCarNo(entity.getCarNo());
        if (bdSerialNumber != null && !StringUtils.equalsIgnoreCase(entity.getId(), bdSerialNumber.getId())) {
            throw new AppException("该车牌已配置序列号", ResultEnum.DATA_VALIDATION_ERROR);
        }
        bdSerialNumber = this.findByDeviceId(entity.getDeviceId());
        if (bdSerialNumber != null && !StringUtils.equalsIgnoreCase(entity.getId(), bdSerialNumber.getId())) {
            throw new AppException("该设备已配置序列号", ResultEnum.DATA_VALIDATION_ERROR);
        }

        // 保存数据
        if (StringUtils.isBlank(entity.getId())) {
            // 新增序列号
            entity.setCreateDate(now);
            entity.setUpdateDate(now);
            entity.setDelFlag(0);
            bdSerialNumber = bdSerialNumberRepository.saveAndFlush(entity);

            // 取出该车牌关联的汽车, 修改其序列号
            SBDevice device = findRelationDevice(entity);
            if (null != device) {
                device.setUpdateDate(now);
                device.setSerialNumberId(bdSerialNumber.getId());
            }
        } else {
            entity.setUpdateDate(now);
            // 更新设备
            bdSerialNumber = bdSerialNumberRepository.saveAndFlush(entity);
        }


        return bdSerialNumber;
    }

    @Override
    public void delete(BDSerialNumber entity) throws AppException {
        entity.setDelFlag(1);
        entity.setUpdateDate(new Date());

        // 序列号删除后, 清除关联设备的序列号
        // 取出该车牌关联的汽车, 修改其序列号
        SBDevice relationDevice = findRelationDevice(entity);
        relationDevice.setSerialNumberId(null);

        super.delete(entity);

        sbDeviceService.saveAndFlush(relationDevice);
    }

    /**
     * 获取序列号关联的设备
     *
     * @param entity
     * @return
     */
    private SBDevice findRelationDevice(BDSerialNumber entity) {
        SBDevice device = null;

        if (StringUtils.isNotBlank(entity.getCarNo())) {
            device = sbDeviceService.findByCarNo(entity.getCarNo());
        }
        if (device == null && StringUtils.isNotBlank(entity.getDeviceId())) {
            device = sbDeviceService.findByDeviceId(entity.getDeviceId()).orElse(null);
        }

        return device;
    }
}
