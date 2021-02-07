package com.baidu.shunba.biz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sb_shift")
public class SBShiftEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 325714421940012452L;

	private String id;
	private String shiftNo;
	
	private Date createDate;
	private Date updateDate;
	
	private Integer seq;
	private String name;
	
	
	private String lineId;
	
	private String startTime;
	private String endTime;
	private String carNo;

	private String driverId;
	private String driverName;
	private String driverPhone;
	
	

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

	@Column(name = "shift_no", length = 36)
	public String getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
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

	@Column(name = "seq")
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "line_id", length = 36)
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	@Column(name = "start_time", length = 50)
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time", length = 50)
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "car_no", length = 50)
	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	@Column(name = "driver_id", length = 36)
	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	@Column(name = "driver_name", length = 100)
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Column(name = "driver_phone", length = 50)
	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	
	
}