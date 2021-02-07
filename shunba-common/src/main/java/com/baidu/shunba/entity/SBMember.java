package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_member")
public class SBMember {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    private String id;
    private String memberId;

    /**
     * 乘客类型. <br/>
     * 1: 乘客, 2: 司机
     */
    private int memberType;

    private Date createDate;
    private Date updateDate;

    private String name;
    private String phone;
    private String feature;

    private Double faceProbability;
    private Integer hasImage;
}
