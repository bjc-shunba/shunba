package com.baidu.shunba.biz.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * sb_driver_dispatch_record
 */
@Data
@Entity
@Table(name = "sb_driver_dispatch_record")
public class SbDriverDispatchRecord {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    public String id;

    /**
     * 班次编号
     */
    @Column(name ="shift_no")
    public String shiftNo;

    /**
     * 关联的设备id
     */
    @Column(name ="device_id")
    public String deviceId;

    /**
     * 操作（0-发车 1-收车）
     */
    @Column(name ="operate")
    public int operate;

    /**
     * 收到收发车信号的时间
     */
    @Column(name ="dispatch_time")
    public Date dispatchTime;
}
