package com.baidu.shunba.biz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sb_app_version")
public class SBAppVersionEntity implements java.io.Serializable {

	
	private static final long serialVersionUID = 3841836764399299823L;

	private String id;
	
	private Date createDate;
	private Date updateDate;
	
	private String version;
	private Integer verMain;
	private Integer verSub;
	private Integer verThird;
	
	private String md5;
	private String memo;
	
	private Integer isUrgent;
	
	private Integer isUploaded;
	
	private Integer delFlag;

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

	@Column(name = "version", length = 50)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Column(name = "ver_main")
	public Integer getVerMain() {
		return verMain;
	}

	public void setVerMain(Integer verMain) {
		this.verMain = verMain;
	}

	@Column(name = "ver_sub")
	public Integer getVerSub() {
		return verSub;
	}

	public void setVerSub(Integer verSub) {
		this.verSub = verSub;
	}

	@Column(name = "ver_third")
	public Integer getVerThird() {
		return verThird;
	}

	public void setVerThird(Integer verThird) {
		this.verThird = verThird;
	}

	@Column(name = "md5", length = 100)
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "is_urgent")
	public Integer getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Integer isUrgent) {
		this.isUrgent = isUrgent;
	}

	@Column(name = "is_uploaded")
	public Integer getIsUploaded() {
		return isUploaded;
	}

	public void setIsUploaded(Integer isUploaded) {
		this.isUploaded = isUploaded;
	}

	@Column(name = "del_flag")
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
}
