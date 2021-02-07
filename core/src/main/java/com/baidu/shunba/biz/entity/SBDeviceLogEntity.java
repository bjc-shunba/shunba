package com.baidu.shunba.biz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sb_device_log")
public class SBDeviceLogEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = -4233506619896560732L;

	private String id;
	
	private Date createDate;
	
	private String deviceId;
	private String osVersion;
	private String appVersion;
	private String data;
	

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

	@Column(name = "device_id", length = 255)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@Column(name = "os_version", length = 100)
	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	@Column(name = "app_version", length = 100)
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Column(name = "data", length = 1024)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	
	
}