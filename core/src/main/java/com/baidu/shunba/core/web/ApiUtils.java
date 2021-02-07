package com.baidu.shunba.core.web;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.baidu.shunba.common.utils.DateUtils;
import com.baidu.shunba.common.utils.MD5Utils;
import com.baidu.shunba.common.utils.PropertiesUtil;
import com.baidu.shunba.core.config.RuntimeConfig;

public class ApiUtils {

	private static final int V_START = 4;
	private static final int V_END = 9;
	
	private static final String TEST_AUTH_KEY = "TEST4DEVAUTHKEYSUPERMANVSBATMAN";
	
//	public static String getPostStringFromRequest(HttpServletRequest request) throws Exception {
//		ApiRequestWrapper requestWrapper = new ApiRequestWrapper(request);
//		return requestWrapper.getBody();
//	}

	public static void writeResponse(HttpServletResponse response, String content) {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("expires", "0");
		response.setCharacterEncoding("UTF-8");

		//
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(content);
		} catch (Exception e) {
		}
		if (pw != null) {
			pw.flush();
		}
	}

	// 验证
	private static boolean verifyAuthKey(long time, String publicKey, String AuthKey1) {
		if (RuntimeConfig.isStrictApi()) {
			if (Math.abs(System.currentTimeMillis() - time) > RuntimeConfig.getApiTimeout() * 60 * 1000l) {
				return false;
			}
		}
		
		String auth = generateAuthToken(publicKey, time);

		if (auth.equalsIgnoreCase(publicKey + "_" + AuthKey1)) {
			return true;
		}

		return false;
	}

	public static boolean simpleVerifyAuthToken(long time, String keyString) {
		if (keyString == null || keyString.trim().length() == 0) {
			return false;
		}
		
		if (time < 0) {
			return false;
		}

		if (RuntimeConfig.isDebug() && keyString.equals(TEST_AUTH_KEY)) {
			return true;
		}
		
		String[] aa = keyString.split("\\_");
		if (aa.length != 2) {
			return false;
		}
		
		return verifyAuthKey(time, aa[0], aa[1]);
	}

	public static String generateAuthToken(final String publicKey, final Date date) {
		return generateAuthToken(RuntimeConfig.AuthTokenPrivate(), publicKey, RuntimeConfig.getApiVersion(), date, V_START, V_END);
	}

	public static String generateAuthToken(final String publicKey, final long time) {
		return generateAuthToken(RuntimeConfig.AuthTokenPrivate(), publicKey, RuntimeConfig.getApiVersion(), time, V_START, V_END);
	}

	
	
	
	public static String generateAuthToken(final String privateKey, final String publicKey, final String version, final long timestamp, final int tokenBegin, final int tokenEnd) {
		return generateAuthToken(privateKey, publicKey, version, new Date(timestamp), tokenBegin, tokenEnd);
	}
	
	public static String generateAuthToken(final String privateKey, final String publicKey, final String version, final Date date, final int tokenBegin, final int tokenEnd) {
		SimpleDateFormat simpleDateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = simpleDateFormater.format(date);
		
		String v = version;
		if (v == null) {
			v = "";
		}
		String s = publicKey + time + privateKey + v;
		String md5_s = MD5Utils.MD5(s);
		String v_Key = md5_s.substring(tokenBegin, tokenEnd);
		return publicKey + "_" + v_Key;
	}
	
}
