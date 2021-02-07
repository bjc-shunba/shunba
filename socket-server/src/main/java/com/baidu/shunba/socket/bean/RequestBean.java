package com.baidu.shunba.socket.bean;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.cglib.beans.BeanMap;

import com.baidu.shunba.common.gson.GsonUtils;

public class RequestBean {
	
	public final String key;    //请求唯一标识，自定义
	public long t;    //时间戳
	
	private transient SocketAction action;    //动作，命令
	private final String m;    //动作名，命令名
	
	private Object o;    //传递内容

	public RequestBean(String key, SocketAction action) {
		this.key = key;
		this.t = System.currentTimeMillis();
		this.action = action;
		m = action.name();
	}
	
	public SocketAction getAction() {
		if (action == null) {
			action = SocketAction.getAction(m);
		}
		return action;
	}

	public <T extends Object> T getContent(Type type) {
		try {
			if (o == null) {
				return null;
			}
			if (o.getClass() == type) {
				return (T) o;
			}
			o = GsonUtils.fromJson(GsonUtils.toJson(o), type);
			return (T) o;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setContent(Object o) {
		this.o = o;
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

	public static RequestBean fromString(String string) {
		RequestBean request = GsonUtils.fromJson(string, RequestBean.class);
		return request;
	}
	
	public static void main(String[] args) {
		RequestBean bean = new RequestBean("123", SocketAction.sign);
		bean.o = "aaabbb";
		
		System.out.println(bean);
	}
}
