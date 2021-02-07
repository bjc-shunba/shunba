package com.baidu.shunba.dao;

import com.baidu.shunba.bean.DeviceInitializationInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceInitializationInformationRepository extends JpaRepository<DeviceInitializationInformation, String> {
    @Query(value = "SELECT d.id, d.device_id, d.shift_no as shift_no, d.car_no as car_no, s.line_id as line_id, s.driver_id as driver_id, s.driver_name as driver_name, n.serial_number as license "
            + "FROM sb_device d "
            + "LEFT JOIN sb_shift s "
            + "ON d.shift_no = s.shift_no "
            + "LEFT JOIN bd_serial_number n "
            + "ON d.device_id = n.device_id AND n.del_flag = 0 "
            + "WHERE d.device_id = ?1", nativeQuery = true, name = "DeviceInitializationInformationResults")
    DeviceInitializationInformation findByDeviceId(String deviceId);
}
