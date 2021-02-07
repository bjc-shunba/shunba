package com.baidu.shunba.biz.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sb_device")
public class SBDeviceEntity implements java.io.Serializable {

    private static final long serialVersionUID = -4233506619896560732L;

    private String id;

    private Date createDate;
    private Date updateDate;

    private String deviceId;
    private String osVersion;
    private String appVersion;
    private Long space;

    private Date lastLogonTime;
    private Date lastLogoffTime;

    private Integer isOnline;
    private Integer delFlag;

    private String lineId;
    private String shiftNo;

    private String serialNumberId;

    private Date lastGetMemberTime;
    private Date lastGetTicketTime;
    private Date lastPostTicketTime;

    private Integer lastSeq;

    /**
     * 关联车牌号
     */
    private String carNo;

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
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

    @Column(name = "space")
    public Long getSpace() {
        return space;
    }

    public void setSpace(Long space) {
        this.space = space;
    }

    @Column(name = "last_logon_time")
    public Date getLastLogonTime() {
        return lastLogonTime;
    }

    public void setLastLogonTime(Date lastLogonTime) {
        this.lastLogonTime = lastLogonTime;
    }

    @Column(name = "last_logoff_time")
    public Date getLastLogoffTime() {
        return lastLogoffTime;
    }

    public void setLastLogoffTime(Date lastLogoffTime) {
        this.lastLogoffTime = lastLogoffTime;
    }

    @Column(name = "is_online")
    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    @Column(name = "del_flag")
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Column(name = "line_id", length = 36)
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Column(name = "shift_id", length = 36)
    public String getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(String shiftNo) {
        this.shiftNo = shiftNo;
    }

    @Column(name = "serial_number_id", length = 36)
    public String getSerialNumberId() {
        return serialNumberId;
    }

    public void setSerialNumberId(String serialNumberId) {
        this.serialNumberId = serialNumberId;
    }

    @Column(name = "last_seq")
    public Integer getLastSeq() {
        return lastSeq;
    }

    public void setLastSeq(Integer lastSeq) {
        this.lastSeq = lastSeq;
    }

    @Column(name = "last_get_member_time")
    public Date getLastGetMemberTime() {
        return lastGetMemberTime;
    }

    public void setLastGetMemberTime(Date lastGetMemberTime) {
        this.lastGetMemberTime = lastGetMemberTime;
    }

    @Column(name = "last_get_ticket_time")
    public Date getLastGetTicketTime() {
        return lastGetTicketTime;
    }

    public void setLastGetTicketTime(Date lastGetTicketTime) {
        this.lastGetTicketTime = lastGetTicketTime;
    }

    @Column(name = "last_post_ticket_time")
    public Date getLastPostTicketTime() {
        return lastPostTicketTime;
    }

    public void setLastPostTicketTime(Date lastPostTicketTime) {
        this.lastPostTicketTime = lastPostTicketTime;
    }


    private BDSerialNumber serialNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "serial_number_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    public BDSerialNumber getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BDSerialNumber serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "car_no")
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}