package com.baidu.shunba.service;

import com.baidu.shunba.entity.BDSerialNumber;

public interface BDSerialNumberService extends BaseService<BDSerialNumber, String> {
    BDSerialNumber findBySerialNumber(String serialNumber);

    BDSerialNumber findByCarNo(String carNo);

    BDSerialNumber findByDeviceId(String deviceId);
}
