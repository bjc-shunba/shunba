package com.baidu.shunba.server.log;

import com.baidu.shunba.biz.entity.SBDeviceLogEntity;
import com.baidu.shunba.core.persistence.service.SystemService;

public class SBLogUtils {

	
	
	public static void log(SystemService systemService, String deviceId, String content) {
		log(systemService, deviceId, null, null, content);
	}
	
	public static void log(SystemService systemService, String deviceId, String appVersion, String osVersion, String content) {
		SBDeviceLogEntity log = new SBDeviceLogEntity();
		log.setDeviceId(deviceId);
		log.setAppVersion(appVersion);
		log.setOsVersion(osVersion);
		log.setData(content);
		systemService.save(log);
	}
}
