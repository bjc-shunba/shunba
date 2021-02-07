package com.baidu.shunba.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 系统用户表
 */
@Entity
@Table(name = "t_s_user")
@PrimaryKeyJoinColumn(name = "id")
public class TSUser extends TSBaseUser implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String signatureFile;// 签名文件
	private String mobilePhone;// 手机
	private String officePhone;// 办公电话
	private String email;// 邮箱
	private Integer secondaryAuthority;
	private Integer authorityDept;
	private String dptId;
	
	private String employeeId;
	private java.lang.Integer authorityStore;
	private java.lang.Integer authoritySupplier;
	private java.lang.Integer authorityRepo;
	
	private java.lang.Integer allowMobPos;	//是否允许移动POS
	private java.lang.Integer mobPosStoreId;	//移动POS对应操作的门店ID
	private java.lang.String mobPosStoreName;	//移动POS对应操作的门店名称
	private java.lang.Integer mobPosStoreIsClose;	//移动POS对应操作的门店名称
	
	@Transient
	public java.lang.Integer getAllowMobPos() {
		return allowMobPos;
	}

	public void setAllowMobPos(java.lang.Integer allowMobPos) {
		this.allowMobPos = allowMobPos;
	}

	@Transient
	public java.lang.Integer getMobPosStoreId() {
		return mobPosStoreId;
	}

	public void setMobPosStoreId(java.lang.Integer mobPosStoreId) {
		this.mobPosStoreId = mobPosStoreId;
	}
	
	@Transient
	public java.lang.String getMobPosStoreName() {
		return mobPosStoreName;
	}

	public void setMobPosStoreName(java.lang.String mobPosStoreName) {
		this.mobPosStoreName = mobPosStoreName;
	}
	
	@Transient
	public java.lang.Integer getMobPosStoreIsClose() {
		return mobPosStoreIsClose;
	}

	public void setMobPosStoreIsClose(java.lang.Integer mobPosStoreIsClose) {
		this.mobPosStoreIsClose = mobPosStoreIsClose;
	}

	@Column(name ="dpt_id",nullable=false,length=32)
	public String getDptId() {
		return dptId;
	}

	public void setDptId(String dptId) {
		this.dptId = dptId;
	}
	
	@Column(name ="authoritydept",nullable=false,length=20)
	public Integer getAuthorityDept() {
		return authorityDept;
	}

	public void setAuthorityDept(Integer authorityDept) {
		this.authorityDept = authorityDept;
	}
	
	@Column(name ="secondary_authority",nullable=false,length=20)
	public Integer getSecondaryAuthority() {
		return secondaryAuthority;
	}

	public void setSecondaryAuthority(Integer secondaryAuthority) {
		this.secondaryAuthority = secondaryAuthority;
	}
	
	@Column(name ="employee_id",nullable=false,length=20)
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	@Column(name = "authorityStore", length = 100)
	public Integer getAuthorityStore() {
		return this.authorityStore;
	}

	public void setAuthorityStore(Integer authorityStore) {
		this.authorityStore = authorityStore;
	}
	
	@Column(name ="AUTHORITYREPO",nullable=true,length=11)
	public java.lang.Integer getAuthorityRepo() {
		return authorityRepo;
	}
    
	public void setAuthorityRepo(java.lang.Integer authorityRepo) {
		this.authorityRepo = authorityRepo;
	}

	@Column(name ="AUTHORITYSUPPLIER",nullable=true,length=11)
	public java.lang.Integer getAuthoritySupplier() {
		return authoritySupplier;
	}

	public void setAuthoritySupplier(java.lang.Integer authoritySupplier) {
		this.authoritySupplier = authoritySupplier;
	}

	/**创建时间*/
	private java.util.Date createDate;
	/**创建人ID*/
	private java.lang.String createBy;
	/**创建人名称*/
	private java.lang.String createName;
	/**修改时间*/
	private java.util.Date updateDate;
	/**修改人*/
	private java.lang.String updateBy;
	/**修改人名称*/
	private java.lang.String updateName;
	
	@Column(name = "signatureFile", length = 100)
	public String getSignatureFile() {
		return this.signatureFile;
	}

	public void setSignatureFile(String signatureFile) {
		this.signatureFile = signatureFile;
	}

	@Column(name = "mobilePhone", length = 30)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "officePhone", length = 20)
	public String getOfficePhone() {
		return this.officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="create_date",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人ID
	 */
	@Column(name ="create_by",nullable=true,length=32)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人ID
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="create_name",nullable=true,length=32)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  修改时间
	 */
	@Column(name ="update_date",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  修改时间
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修改人ID
	 */
	@Column(name ="update_by",nullable=true,length=32)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修改人ID
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修改人名称
	 */
	@Column(name ="update_name",nullable=true,length=32)
	public java.lang.String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修改人名称
	 */
	public void setUpdateName(java.lang.String updateName){
		this.updateName = updateName;
	}
	
	
	
	
}