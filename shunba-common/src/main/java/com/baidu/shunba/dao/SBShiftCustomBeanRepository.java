package com.baidu.shunba.dao;

import com.baidu.shunba.bean.SBShiftCustomBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface SBShiftCustomBeanRepository extends JpaRepository<SBShiftCustomBean, String>, JpaSpecificationExecutor<SBShiftCustomBean> {
}
