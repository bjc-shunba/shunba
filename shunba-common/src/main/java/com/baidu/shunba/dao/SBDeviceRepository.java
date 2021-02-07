package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SBDeviceRepository extends JpaRepository<SBDevice, String>, JpaSpecificationExecutor<SBDevice> {
    @Query("SELECT d FROM SBDevice d WHERE d.deviceId = ?1 and (d.delFlag is null OR d.delFlag <> 1)")
    Optional<SBDevice> findByDeviceId(String deviceId);

    @Query("SELECT d FROM SBDevice d WHERE d.carNo = ?1 and (d.delFlag is null OR d.delFlag <> 1)")
    List<SBDevice> findAllByCarNo(String carNo);

    @Query("SELECT d FROM SBDevice d WHERE d.deviceId = ?1 and (d.delFlag is null OR d.delFlag <> 1)")
    List<SBDevice> findAllByDeviceId(String DeviceId);

    @Query("SELECT d FROM SBDevice d WHERE d.carNo = ?1 and (d.delFlag is null OR d.delFlag <> 1)")
    Optional<SBDevice> findByCarNo(String carNo);

    @Query("SELECT d FROM SBDevice d WHERE d.shiftNo = ?1 and (d.delFlag is null OR d.delFlag <> 1)")
    List<SBDevice> findAllByShiftNo(String shiftId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sb_device SET app_version = ?2, os_version = ?3 WHERE device_id = ?1 AND (app_version is NULL OR app_version <> ?2 OR os_version is NULL OR os_version <> ?3)", nativeQuery = true)
    int updateVersions(String deviceId, String appVersion, String osVersion);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SBDevice d SET d.lastPostTicketTime = ?2 WHERE d.deviceId = ?1")
    int updateLastPostTicketTime(String deviceId, Date postTicketTime);
}
