package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBDriverClockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SBDriverClockRecordRepository extends JpaRepository<SBDriverClockRecord, String> {
}
