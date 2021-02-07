package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统参数配置, 用于配置设备使用的百度人脸扫描SDK的运行license, 即serialNumber
 */
@Data
@Entity
@Table(name = "bd_serial_number")
public class BDSerialNumber {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    private String serialNumber;

    private Date createDate;

    private Date updateDate;

    private String deviceId;

    private String carNo;

    private Integer isUsed;

    private Date usedTime;

    private Integer delFlag;

    private String reason;
}
