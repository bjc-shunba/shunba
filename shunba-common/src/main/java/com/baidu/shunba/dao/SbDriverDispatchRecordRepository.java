package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SbDriverDispatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SbDriverDispatchRecordRepository extends JpaRepository<SbDriverDispatchRecord, String> {
    Optional<SbDriverDispatchRecord> findTopByShiftNo(String shiftNo);

    Optional<SbDriverDispatchRecord> findTopByShiftNoAndDeviceId(String shiftNo, String deviceId);
}
