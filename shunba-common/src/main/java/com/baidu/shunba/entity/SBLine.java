package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_line")
public class SBLine {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    private String id;
    private String lineId;

    private Date createDate;
    private Date updateDate;

    private String name;

    private Integer seq;

    private Integer nextSeq;
}
