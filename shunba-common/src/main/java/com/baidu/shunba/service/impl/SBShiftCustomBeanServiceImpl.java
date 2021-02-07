package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.SBShiftCustomBean;
import com.baidu.shunba.dao.SBShiftCustomBeanRepository;
import com.baidu.shunba.service.SBShiftCustomBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class SBShiftCustomBeanServiceImpl extends BaseServiceImpl<SBShiftCustomBean, String> implements SBShiftCustomBeanService {
    @Autowired
    private SBShiftCustomBeanRepository sbShiftCustomBeanRepository;

    @Override
    protected JpaRepository<SBShiftCustomBean, String> getJpaRepository() {
        return sbShiftCustomBeanRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBShiftCustomBean> getJpaSpecificationExecutor() {
        return sbShiftCustomBeanRepository;
    }
}
