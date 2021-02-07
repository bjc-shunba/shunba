package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sb_ticket")
public class SBTicket {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    private String ticketNo;
    private String ticketId;

    private Date createDate;
    private Date updateDate;

    private Integer seq;

    private String deviceId;
    private String lineId;
    private String shiftId;
    private String shiftNo;

    private String memberId;
    private String faceId;
    private String userPhone;

    private Integer isCheck;
    private Date checkTime;
    private Integer checkType;
    private Double temperature;
    
    private Integer syncFlag;
    private String syncMsg;

    private Integer delFlag;
}
