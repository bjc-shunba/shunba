package com.baidu.shunba.bean.message;

import com.baidu.shunba.entity.SBShift;
import lombok.Data;

@Data
public class SecondLineShift {
    private String lineId;

    private String name;

    private String shiftNo;

    private String startTime;

    private String endTime;

    private String carNo;

    private String driverId;

    private String driver;

    private String driverPhone;

    private String remark;

    public SBShift setSBShift(SBShift sbShift) {
        sbShift.setName(name);

        sbShift.setShiftNo(shiftNo);
        sbShift.setShiftId(shiftNo);

        sbShift.setLineId(lineId);
        sbShift.setDriverPhone(driverPhone);
        sbShift.setDriverId(driverId);
        sbShift.setDriverName(driver);

        sbShift.setCarNumber(carNo);
        sbShift.setCarNo(carNo);
        sbShift.setStartTime(startTime);
        sbShift.setEndTime(endTime);

        // sbShift.setSchedule();

        return sbShift;
    }
}
