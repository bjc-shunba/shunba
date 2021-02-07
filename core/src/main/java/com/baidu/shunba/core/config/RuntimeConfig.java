package com.baidu.shunba.core.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.shunba.common.utils.ObjectUtils;
import com.baidu.shunba.common.utils.PropertiesUtil;
import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.core.web.bean.MessageJson;


@Configuration
public class RuntimeConfig extends ConfigUtils {
	
	private static boolean IS_DEBUG = true;
	

	public static boolean config(SystemService systemService, Environment env) {
		String[] aps = env.getActiveProfiles();
		if (aps.length > 0) {
			if ("prod".equalsIgnoreCase(aps[0])) {
				RuntimeConfig.IS_DEBUG = false;
			} else if ("dev".equalsIgnoreCase(aps[0])) {
				RuntimeConfig.IS_DEBUG = true;
			}
		}
		RuntimeConfig.overrideCaches(systemService);
		return true;
	}
	
	public static boolean isDebug() {
		return RuntimeConfig.IS_DEBUG;
	}
	
	////
	public static MessageJson clearCache(SystemService systemService, HttpServletRequest request) {
		clearCache();
		List<SysConfigEntity> datas = systemService.getList(SysConfigEntity.class);
		for (SysConfigEntity config : datas) {
			overrideCache(config.getJian(), config.getZhi());
		}
		return MessageJson.newInstance("Finished!");
	}
	
	public static MessageJson getConfigs(SystemService systemService) {
		List<SysConfigEntity> datas = systemService.getList(SysConfigEntity.class);
		return MessageJson.newInstance(datas);
	}
	
	////
	
	public static final long MAX_ORDER_TS = 15 * 60 * 1000l;
	
	protected static final Map<String, Object> cache = new HashMap<String, Object>();
	protected static final Map<String, String> overrideCache = new HashMap<String, String>();
	
	private static PropertiesUtil sysConfigProperties = null;
	public static synchronized PropertiesUtil getSysConfigProperties() {
		if (sysConfigProperties == null) {
			if (isDebug()) {
				sysConfigProperties = new PropertiesUtil("sysConfig-dev.properties");
			} else {
				sysConfigProperties = new PropertiesUtil("sysConfig-prod.properties");
			}
		}
		return sysConfigProperties;
	}
	
	public static synchronized void clearCache() {
		cache.clear();
		sysConfigProperties = null;
	}
	
	public static synchronized void overrideCaches(SystemService systemService) {
		try {
			List<SysConfigEntity> datas = systemService.getList(SysConfigEntity.class);
			for (SysConfigEntity config : datas) {
				overrideCache(config.getJian(), config.getZhi());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void overrideCache(String key, String value) {
		cache.remove(key);
		overrideCache.put(key, value);
	}
	
	private static boolean getBoolean(String key) {
		Object value = cache.get(key);
		if (value == null) {
			if (overrideCache.containsKey(key)) {
				value = ObjectUtils.isStringTrue(overrideCache.get(key));
			} else {
				value = getBooleanProperty(getSysConfigProperties(), key);
			}
			cache.put(key, value);
		}
		return (boolean) value;
	}
	
	private static int getInt(String key) {
		Object value = cache.get(key);
		if (value == null) {
			if (overrideCache.containsKey(key)) {
				value = ObjectUtils.getIntValue(overrideCache.get(key));
			} else {
				value = getIntProperty(getSysConfigProperties(), key);
			}
			cache.put(key, value);
		}
		return (int) value;
	}
	
	private static double getDouble(String key) {
		Object value = cache.get(key);
		if (value == null) {
			if (overrideCache.containsKey(key)) {
				value = ObjectUtils.getDoubleValue(overrideCache.get(key));
			} else {
				value = getDoubleProperty(getSysConfigProperties(), key);
			}
			cache.put(key, value);
		}
		return (double) value;
	}
	
	private static long getLong(String key) {
		Object value = cache.get(key);
		if (value == null) {
			if (overrideCache.containsKey(key)) {
				value = ObjectUtils.getLongValue(overrideCache.get(key));
			} else {
				value = getLongProperty(getSysConfigProperties(), key);
			}
			cache.put(key, value);
		}
		return (long) value;
	}
	
	private static String getString(String key) {
		Object value = cache.get(key);
		if (value == null) {
			if (overrideCache.containsKey(key)) {
				value = overrideCache.get(key);
			} else {
				value = getStringProperty(getSysConfigProperties(), key);
			}
			cache.put(key, value);
		}
		return (String) value;
	}
	
	public static String AuthTokenPrivate() {
		return getString("AuthToken_Private");
	}
	
	public static boolean isStrictApi() {
		return getBoolean("STRICT_API");
	}
	
	
	public static long getApiTimeout() {
		return getLong("API_Timeout");
	}
	
	public static String getApiVersion() {
		return getString("API_Version");
	}
	
	public static String getUploadFolder() {
		return getString("UPLOAD_FOLDER");
	}
	
	public static String getShunbaSocketServer() {
		return getString("ShunbaSocketServer");
	}
	
	public static String getShunbaSocketServerDomain() {
		return getString("ShunbaSocketServerDomain");
	}
	
	public static int getShunbaSocketServerPort() {
		return getInt("ShunbaSocketServerPort");
	}
	
	public static String getShunbaServer() {
		return getString("ShunbaServer");
	}
}
