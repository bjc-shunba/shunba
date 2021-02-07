package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 设备信息
 */
@Data
@Entity
@Table(name = "sb_device")
public class SBDevice {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    private Date createDate;
    private Date updateDate;

    private String deviceId;
    /**
     * 系统版本号，即安卓版本号
     */
    private String osVersion;
    /**
     * 应用版本号
     */
    private String appVersion;
    private Long space;

    private Date lastLogonTime;
    private Date lastLogoffTime;

    private Integer isOnline;
    /**
     * 是否禁用. 1:禁用;
     */
    private int delFlag;

    private String lineId;

    @Column(name = "shift_no")
    private String shiftNo;

    private String carNo;

    @Column(name = "serial_number_id")
    private String serialNumberId;

    private Date lastGetMemberTime;
    private Date lastGetTicketTime;
    private Date lastPostTicketTime;

    private Integer lastSeq;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "serial_number_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BDSerialNumber serialNumber;
}
