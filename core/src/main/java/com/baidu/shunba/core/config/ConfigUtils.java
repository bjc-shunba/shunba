package com.baidu.shunba.core.config;

import com.baidu.shunba.common.utils.ObjectUtils;
import com.baidu.shunba.common.utils.PropertiesUtil;

public class ConfigUtils {

	public static boolean getBooleanProperty(PropertiesUtil pro, String key) {
		return ObjectUtils.isStringTrue(getStringProperty(pro, key));
	}
	
	public static int getIntProperty(PropertiesUtil pro, String key) {
		return ObjectUtils.getIntValue(getStringProperty(pro, key));
	}
	
	public static long getLongProperty(PropertiesUtil pro, String key) {
		return ObjectUtils.getLongValue(getStringProperty(pro, key));
	}
	
	public static double getDoubleProperty(PropertiesUtil pro, String key) {
		return ObjectUtils.getDoubleValue(getStringProperty(pro, key));
	}
	
	public static String getStringProperty(PropertiesUtil pro, String key) {
		return pro.readProperty(key);
	}
	
	
}
