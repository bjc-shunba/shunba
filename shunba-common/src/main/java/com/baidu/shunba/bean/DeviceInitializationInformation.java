package com.baidu.shunba.bean;

import lombok.Data;

import javax.persistence.*;

/**
 * pad设备运行必备参数
 */
@SqlResultSetMapping(
        name = "DeviceInitializationInformationResults",
        entities = {
                @EntityResult(
                        entityClass = DeviceInitializationInformation.class,
                        fields = {
                                @FieldResult(name = "deviceId", column = "device_id"),
                                @FieldResult(name = "id", column = "id"),
                                @FieldResult(name = "shiftNo", column = "shift_no"),
                                @FieldResult(name = "carNo", column = "car_no"),
                                @FieldResult(name = "lineId", column = "line_id"),
                                @FieldResult(name = "driverId", column = "driver_id"),
                                @FieldResult(name = "driverName", column = "driver_name"),
                                @FieldResult(name = "license", column = "license")
                        }
                )
        }
)
@Data
@Entity
@Table(name = "sb_device")
public class DeviceInitializationInformation {
    @Id
    private String id;

    /**
     * 设备最新的应用版本号 (来自sb_app_version, version, 使用getLatestVersion获取最新版本)
     */
    @Transient
    private String newVersion;

    /**
     * 车载pad设备最新版本应用的描述信息 (来自sb_app_version, memo)
     */
    @Transient
    private String versionDescription;

    /**
     * 车载pad设备, 使用的百度扫码SDK所需的license序列号 (来自bd_serial_number, 使用car_no以及device_id关联该表)
     */
    private String license;

    /**
     * device关联的车, 当前运行班次号 (来自sb_device, shift_no, 使用该id可以关联sb_shift中的shift_id或者shift_no)
     */
    @Column(name = "shift_no")
    private String shiftNo;

    /**
     * 车载pad设备关联的车车牌号码 (来自sb_device, car_no)
     */
    private String carNo;

    /**
     * device关联的车, 当前运行路线 (来自sb_shift, line_id)
     */
    private String lineId;

    /**
     * 当前班次司机id (来自sb_shift, driver_id)
     */
    private String driverId;

    /**
     * 当前班次司机姓名 (来自sb_shift, driver_name)
     */
    private String driverName;

    /**
     * 人脸超时时间. 单位: 秒 (运行参数, 来自sys-config)
     */
    @Transient
    private Integer faceTimeout;

    /**
     * 二维码超时时间. 单位: 秒 (运行参数, 来自sys-config)
     */
    @Transient
    private Integer qrcodeTimeout;

    /**
     * 司机温控开关. 0 - 关闭; 1- 开启 (运行参数, 来自sys-config)
     */
    @Transient
    private Integer driverTemperatureCheck;

    /**
     * 乘客温控开关. 0 - 关闭; 1- 开启 (运行参数, 来自sys-config)
     */
    @Transient
    private Integer passengerTemperatureCheck;

    /**
     * 高温阀值. 单位:摄氏度 (运行参数, 来自sys-config)
     */
    @Transient
    private Double temperatureHigh;
}
