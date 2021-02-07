package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_app_version")
public class SBAppVersion {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    private Date createDate;
    private Date updateDate;
    /**
     * 版本下发时间
     */
    private Date useDate;

    private String version;
    private Integer verMain;
    private Integer verSub;
    private Integer verThird;

    private String md5;
    private String memo;

    private Integer isUrgent;

    private Integer isUploaded;

    private Integer delFlag;

    @Transient
    private int count;
}
