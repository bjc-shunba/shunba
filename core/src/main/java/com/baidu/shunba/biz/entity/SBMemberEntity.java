package com.baidu.shunba.biz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sb_member")
public class SBMemberEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 6070910251903917331L;

	private String id;
	private String memberId;
	
	private Date createDate;
	private Date updateDate;
	
	private String name;
	private String phone;
	private String feature;
	
	private Double faceProbability;
	private Integer hasImage;
	

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
	
	@Column(name = "member_id", length = 36)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "phone", length = 50)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "feature")
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	@Column(name = "face_probability")
	public Double getFaceProbability() {
		return faceProbability;
	}

	public void setFaceProbability(Double faceProbability) {
		this.faceProbability = faceProbability;
	}

	@Column(name = "has_image")
	public Integer getHasImage() {
		return hasImage;
	}

	public void setHasImage(Integer hasImage) {
		this.hasImage = hasImage;
	}

	
	
	
}