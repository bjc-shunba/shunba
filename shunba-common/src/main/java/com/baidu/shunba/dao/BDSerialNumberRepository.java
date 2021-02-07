package com.baidu.shunba.dao;

import com.baidu.shunba.entity.BDSerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BDSerialNumberRepository extends JpaRepository<BDSerialNumber, String>, JpaSpecificationExecutor<BDSerialNumber> {
    @Query("SELECT s FROM BDSerialNumber s WHERE s.serialNumber = ?1 and (s.delFlag is null OR s.delFlag <> 1)")
    List<BDSerialNumber> findBySerialNumber(String serialNumber);

    @Query("SELECT s FROM BDSerialNumber s WHERE s.carNo = ?1 and (s.delFlag is null OR s.delFlag <> 1)")
    List<BDSerialNumber> findByCarNo(String carNo);

    @Query("SELECT s FROM BDSerialNumber s WHERE s.deviceId = ?1 and (s.delFlag is null OR s.delFlag <> 1)")
    List<BDSerialNumber> findByDeviceId(String deviceId);
}
