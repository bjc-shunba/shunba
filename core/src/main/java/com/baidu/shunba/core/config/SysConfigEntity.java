package com.baidu.shunba.core.config;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sys_config")
public class SysConfigEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 6421866077311665601L;

	private String id;
	
	private Date createDate;
	private Date updateDate;
	
	private String jian;
	private String name;
	private String zhi;
	

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "jian", length = 100)
	public String getJian() {
		return jian;
	}

	public void setJian(String jian) {
		this.jian = jian;
	}

	@Column(name = "zhi", length = 255)
	public String getZhi() {
		return zhi;
	}

	public void setZhi(String zhi) {
		this.zhi = zhi;
	}

	
	
}