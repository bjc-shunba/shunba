package com.baidu.shunba.biz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "sb_driver_clock_record")
public class SBDriverClockRecordEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = -4233506619896560732L;

	private String id;
	
	private Date createDate;
	
	private String deviceId;
	private String driverId;
	private Double temperature;
	private Date clockTime;
	

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

	@Column(name = "driver_id")
	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	@Column(name = "temperature")
	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	@Column(name = "clock_time")
	public Date getClockTime() {
		return clockTime;
	}

	public void setClockTime(Date clockTime) {
		this.clockTime = clockTime;
	}
}