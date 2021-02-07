package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SysConfigRepository extends JpaRepository<SysConfig, String> {
    SysConfig findByJian(String jian);

    List<SysConfig> findAllByJianIn(List<String> jian);

    void deleteByJian(String jian);
}
