package com.baidu.shunba.common.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baidu.shunba.common.gson.GsonUtils;

public class RequestUtils {

	public static Map<String, String> getRequestParameters(HttpServletRequest request) {
		Map<String, String> requestParams = new HashMap<>();
		try {
			String requestBody = HttpUtils.getRequestPostString(request);

			if (StringUtils.isNotEmpty(requestBody) && StringUtils.isNotEmpty(requestBody.trim())) {
				requestParams = HttpUtils.getUrlParamMap(requestBody);
			} else {
				requestParams = new HashMap<>();
				Enumeration<String> paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String key = paramNames.nextElement();
					requestParams.put(key, request.getParameter(key));
				}
			}
		} catch (Exception e) {
		}

		return requestParams;
	}
	
	
	public static String getRequestString(HttpServletRequest request) {
		try {
			String requestBody = HttpUtils.getRequestPostString(request);

			if (StringUtils.isNotEmpty(requestBody) && StringUtils.isNotEmpty(requestBody.trim())) {
				return requestBody;
			} else {
				Map<String, String> requestParams = new HashMap<>();
				Enumeration<String> paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String key = paramNames.nextElement();
					requestParams.put(key, request.getParameter(key));
				}
				return GsonUtils.toJson(requestParams);
			}
		} catch (Exception e) {
		}

		return null;
	}

}
