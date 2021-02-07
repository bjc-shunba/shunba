package com.baidu.shunba.common.utils;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		ApplicationContextUtil.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}
	
	public static String getMessage(String key) {
		return getMessagePrivate(key, null, LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String key, Object... params) {
		try {
			return String.format(getMessagePrivate(key, null, LocaleContextHolder.getLocale()), params);
		} catch (Exception e) {
			e.printStackTrace();
			return key;
		}
	}
	
	public static String getSpMessage(String key, Object[] params) {
		return getMessagePrivate(key, params, LocaleContextHolder.getLocale());
	}
	
	public static String getSpMessage(String key, Object[] params, Locale locale) {
		return getMessagePrivate(key, params, locale);
	}
	
	private static String getMessagePrivate(String key, Object[] params, Locale locale) {
		try {
			return ApplicationContextUtil.getContext().getMessage(key, params, locale);
		} catch (Exception e) {
			e.printStackTrace();
			return key;
		}
	}
}