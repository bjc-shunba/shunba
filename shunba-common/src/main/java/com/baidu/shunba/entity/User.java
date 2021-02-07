package com.baidu.shunba.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    private String id;

    @Column(name = "username")
    private String userName;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String mobile;

    @Column
    private String description;

    /**
     * 是否已删除Y：已删除，N：未删除
     */
    @Column
    private String deleted;
}
