package com.baidu.shunba.common.jackson;

import com.baidu.shunba.common.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {

	public static String toJson(Object object) {
		try {
			ObjectMapper mapper = new ObjectMapper();
	        return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ObjectUtils.getString(object);
	}
}
