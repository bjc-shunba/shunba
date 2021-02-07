package com.baidu.shunba.biz.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_device_log")
public class SbDriverClockRecord {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    private Date createDate;

    private String deviceId;

    private String driverId;

    private float temperature;

    private Date clockTime;
}
