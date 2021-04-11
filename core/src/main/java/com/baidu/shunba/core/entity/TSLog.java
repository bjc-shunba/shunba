package com.baidu.shunba.core.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_s_log")
public class TSLog extends IdEntity implements java.io.Serializable {
	private Integer loglevel;
	private Date operatetime;
	private Integer operatetype;
	private String username;
	private String logcontent;
	private String broswer;//用户浏览器类型
	private String note;

	@Column(name = "loglevel")
	public Integer getLoglevel() {
		return this.loglevel;
	}

	public void setLoglevel(int loglevel) {
		this.loglevel = loglevel;
	}

	@Column(name = "operatetime", nullable = false, length = 35)
	public Date getOperatetime() {
		return this.operatetime;
	}

	public void setOperatetime(Date operatetime) {
		this.operatetime = operatetime;
	}

	@Column(name = "operatetype")
	public Integer getOperatetype() {
		return this.operatetype;
	}

	public void setOperatetype(Integer operatetype) {
		this.operatetype = operatetype;
	}

	@Column(name = "username", nullable = false, length = 2000)
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "logcontent", nullable = false, length = 2000)
	public String getLogcontent() {
		return this.logcontent;
	}

	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}

	@Column(name = "note", length = 300)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Column(name = "broswer", length = 100)
	public String getBroswer() {
		return broswer;
	}

	public void setBroswer(String broswer) {
		this.broswer = broswer;
	}

}