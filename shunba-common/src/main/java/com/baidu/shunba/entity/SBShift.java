package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 班车信息
 */
@Data
@Entity
@Table(name = "sb_shift")
public class SBShift {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;
    private String shiftId;
    private String shiftNo;

    private Date createDate;
    private Date updateDate;

    private String startTime;
    private String endTime;

    private Integer seq;
    private String name;

    private String lineId;

    private String schedule;

    private String driverName;
    private String driverPhone;
    private String driverId;

    private String carNo;

    private String carNumber;
}
