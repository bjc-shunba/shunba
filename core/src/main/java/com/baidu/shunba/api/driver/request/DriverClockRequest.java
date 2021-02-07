package com.baidu.shunba.api.driver.request;

import com.baidu.shunba.api.BaseBean;
import com.baidu.shunba.common.utils.DateUtils;

import java.util.Date;

public class DriverClockRequest extends BaseBean {

    public String driverId;
    public double temperature;
    private String timestamp;
    public String remark;

    public void setClockTime(Date date) {
        timestamp = DateUtils.getDateStringWithFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public DriverClockRequest() {
        transCode = "07";
    }
}
