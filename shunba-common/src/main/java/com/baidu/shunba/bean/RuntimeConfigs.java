package com.baidu.shunba.bean;

import lombok.Getter;

/**
 * 车载pad设备, 运行时参数. 数据来自sys-config
 */
@Getter
public class RuntimeConfigs {
    /**
     * 常量记录所有定义的运行时key
     */
    public static final String[] RUNTIME_CONFIG_KEYS = new String[]{"faceTimeout", "qrcodeTimeout", "driverTemperatureCheck", "passengerTemperatureCheck", "temperatureHigh"};

    /**
     * 人脸超时时间. 单位: 秒 (运行参数, 来自sys-config)
     */
    private Integer faceTimeout;

    /**
     * 二维码超时时间. 单位: 秒 (运行参数, 来自sys-config)
     */
    private Integer qrcodeTimeout = 30;

    /**
     * 司机温控开关. 0 - 关闭; 1- 开启 (运行参数, 来自sys-config)
     */
    private Integer driverTemperatureCheck = 0;

    /**
     * 乘客温控开关. 0 - 关闭; 1- 开启 (运行参数, 来自sys-config)
     */
    private Integer passengerTemperatureCheck = 0;

    /**
     * 高温阀值. 单位:摄氏度 (运行参数, 来自sys-config)
     */
    private Double temperatureHigh;

    public void setFaceTimeout(Integer faceTimeout) {
        this.faceTimeout = faceTimeout;
    }

    public void setFaceTimeout(String faceTimeout) {
        try {
            this.faceTimeout = Integer.parseInt(faceTimeout);
        } catch (Exception e) {
        }
    }

    public void setQrcodeTimeout(Integer qrcodeTimeout) {
        this.qrcodeTimeout = qrcodeTimeout;
    }

    public void setQrcodeTimeout(String qrcodeTimeout) {
        try {
            this.qrcodeTimeout = Integer.parseInt(qrcodeTimeout);
        } catch (Exception e) {
        }
    }

    public void setDriverTemperatureCheck(Integer driverTemperatureCheck) {
        this.driverTemperatureCheck = driverTemperatureCheck;
    }

    public void setDriverTemperatureCheck(String driverTemperatureCheck) {
        try {
            this.driverTemperatureCheck = Integer.parseInt(driverTemperatureCheck);

            if (this.driverTemperatureCheck > 0) {
                this.driverTemperatureCheck = 1;
            }
        } catch (Exception e) {
        }
    }

    public void setPassengerTemperatureCheck(Integer passengerTemperatureCheck) {
        this.passengerTemperatureCheck = passengerTemperatureCheck;
    }

    public void setPassengerTemperatureCheck(String passengerTemperatureCheck) {
        try {
            this.passengerTemperatureCheck = Integer.parseInt(passengerTemperatureCheck);

            if (this.passengerTemperatureCheck > 0) {
                this.passengerTemperatureCheck = 1;
            }
        } catch (Exception e) {
        }
    }

    public void setTemperatureHigh(Double temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public void setTemperatureHigh(String temperatureHigh) {
        try {
            this.temperatureHigh = Double.parseDouble(temperatureHigh);
        } catch (Exception e) {
        }
    }
}
