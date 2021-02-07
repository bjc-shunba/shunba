package com.baidu.shunba.biz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sb_ticket")
public class SBTicketEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 4797315397493072690L;

	private String id;
	private String ticketNo;
	
	private Date createDate;
	private Date updateDate;
	
	private Integer seq;
	private String lineId;
	private String shiftNo;
	
	private String memberId;

	private Integer delFlag;
	
	private String deviceId;
	private Integer isCheck;
	private Date checkTime;
	private Integer checkType;
	private Double temperature;

	private Integer syncFlag;
	private String syncMsg;

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

	@Column(name = "ticket_no", length = 36)
	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
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

	@Column(name = "line_id", length = 36)
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	@Column(name = "shift_no", length = 36)
	public String getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
	}

	@Column(name = "member_id", length = 36)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	@Column(name = "del_flag")
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Column(name = "is_check")
	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	
	@Column(name = "device_id", length = 255)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "check_time")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "check_type")
	public Integer getCheckType() {
		return checkType;
	}

	public void setCheckType(Integer checkType) {
		this.checkType = checkType;
	}

	@Column(name = "temperature", length = 20, scale = 1)
	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	@Column(name = "sync_flag")
	public Integer getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(Integer syncFlag) {
		this.syncFlag = syncFlag;
	}

	@Column(name = "sync_msg", length = 255)
	public String getSyncMsg() {
		return syncMsg;
	}

	public void setSyncMsg(String syncMsg) {
		this.syncMsg = syncMsg;
	}
}
