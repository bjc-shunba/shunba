package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.RuntimeConfigs;
import com.baidu.shunba.dao.SysConfigRepository;
import com.baidu.shunba.entity.SysConfig;
import com.baidu.shunba.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Override
    public SysConfig findByKey(String key) {
        return sysConfigRepository.findByJian(key);
    }

    @Override
    public List<SysConfig> findAll() {
        return sysConfigRepository.findAll();
    }

    @Override
    public RuntimeConfigs findAllRuntimeConfigs() {
        List<SysConfig> allSysConfigs = this.findAllByKey(Arrays.asList(RuntimeConfigs.RUNTIME_CONFIG_KEYS));

        RuntimeConfigs config = new RuntimeConfigs();
        allSysConfigs.forEach(sysConfig -> {
            switch (sysConfig.getJian()) {
                case "faceTimeout": {
                    config.setFaceTimeout(sysConfig.getZhi());
                    break;
                }
                case "qrcodeTimeout": {
                    config.setQrcodeTimeout(sysConfig.getZhi());
                    break;
                }
                case "driverTemperatureCheck": {
                    config.setDriverTemperatureCheck(sysConfig.getZhi());
                    break;
                }
                case "passengerTemperatureCheck": {
                    config.setPassengerTemperatureCheck(sysConfig.getZhi());
                    break;
                }
                case "temperatureHigh": {
                    config.setTemperatureHigh(sysConfig.getZhi());
                    break;
                }
                default:
            }
        });

        return config;
    }

    @Override
    public List<SysConfig> findAllByKey(List<String> jian) {
        if (null == jian || jian.isEmpty()) {
            return new ArrayList<>();
        }
        return sysConfigRepository.findAllByJianIn(jian);
    }

    @Override
    public SysConfig save(SysConfig sysConfig) {
        Date now = new Date();

        if (sysConfig.getId() == null) {
            sysConfig.setCreateDate(now);
        }
        sysConfig.setUpdateDate(now);

        return sysConfigRepository.saveAndFlush(sysConfig);
    }

    @Override
    public List<SysConfig> saveAll(Collection<SysConfig> datas) {
        if (null == datas || datas.isEmpty()) {
            return new ArrayList<>();
        }

        Date now = new Date();

        List<SysConfig> exists = this.findAllByKey(datas.stream().map(sysConfig -> sysConfig.getJian()).collect(Collectors.toList()));

        datas.forEach(sysConfig -> {
            if (sysConfig.getId() == null) {
                sysConfig.setCreateDate(now);
            }
            sysConfig.setUpdateDate(now);
            for (SysConfig exist : exists) {
                if (Objects.equals(exist.getJian(), sysConfig.getJian())) {
                    sysConfig.setId(exist.getId());
                }
            }
        });

        return sysConfigRepository.saveAll(datas);
    }

    @Override
    public void deleteByKey(String jian) {
        sysConfigRepository.deleteByJian(jian);
    }
}
