package com.baidu.shunba.service;

import com.baidu.shunba.bean.RuntimeConfigs;
import com.baidu.shunba.entity.SysConfig;

import java.util.Collection;
import java.util.List;

public interface SysConfigService {
    SysConfig findByKey(String key);

    List<SysConfig> findAll();

    /**
     * 获取车载pad设备运行时所需系统配置参数
     *
     * @return
     */
    RuntimeConfigs findAllRuntimeConfigs();

    List<SysConfig> findAllByKey(List<String> jian);

    SysConfig save(SysConfig sysConfig);

    List<SysConfig> saveAll(Collection<SysConfig> datas);

    void deleteByKey(String jian);
}
