package com.baidu.shunba.common.gson;

import com.baidu.shunba.common.utils.SUReflectUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class NoSubObjectExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes fieldAtrr) {
		return !SUReflectUtils.isPrimitiveOrWrapClass(fieldAtrr.getDeclaredClass());
	}
	

}
