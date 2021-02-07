package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_driver_dispatch_record")
public class SbDriverDispatchRecord {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    private String id;

    /**
     * 班次编号
     */
    private String shiftNo;

    /**
     * 关联的设备id
     */
    private String deviceId;

    /**
     * 操作（0-发车 1-收车）
     */
    private int operate;

    /**
     * 收到收发车信号的时间
     */
    private Date dispatchTime;
}
