package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_config")
public class SysConfig {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    private String id;

    private Date createDate;
    private Date updateDate;

    private String jian;
    private String name;
    private String zhi;
}
