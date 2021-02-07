package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 司机打卡记录
 */
@Data
@Entity
@Table(name = "sb_driver_clock_record")
public class SBDriverClockRecord {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 司机Id
     */
    private String driverId;

    /**
     * 温度
     */
    private Double temperature;

    /**
     * 打卡时间
     */
    private Date clockTime;
}
