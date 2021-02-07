package com.baidu.shunba.vo;

import lombok.Data;

/**
 * app版本上传参数
 */
@Data
public class AppVersionUploadParam {
    /**
     * 版本号
     */
    private String version;

    /**
     * 版本更新说明
     */
    private String memo;
}
