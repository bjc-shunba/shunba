package com.baidu.shunba.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_shift_custom")
public class SBShiftCustomBean {
    @Id
    private String id;

    private String shiftNo;

    private Date createDate;

    private Date updateDate;

    private Integer seq;

    private String name;

    private String lineId;

    private String startTime;

    private String endTime;

    private String carNo;

    private String driverId;

    private String driverName;

    private String driverPhone;

    private Date startTimeDate;

    private Date endTimeDate;
}
