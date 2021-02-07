package com.baidu.shunba.socket.bean;

import java.lang.reflect.Type;

import com.baidu.shunba.common.gson.GsonUtils;

public class ResponseBean {
	public final String key;    //请求唯一标识，对应request的response应一致
	public long t;    //时间戳
	
	private transient SocketAction action;    //动作，命令
	private final String m;    //动作名，命令名
	
	public Integer c = 1;    //code，返回结果的直观判断 > 0成功，<= 0失败
	public String msg = "ok";    //消息
	
	private Object o;    //传给client的内容
	
	public ResponseBean(String key, SocketAction action) {
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
	
	public boolean isSuccess() {
		return c > 0;
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
	
	public void setCodeMsg(Integer c, String msg) {
		this.c = c;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}
	
	public static ResponseBean fromString(String string) {
		ResponseBean request = GsonUtils.fromJson(string, ResponseBean.class);
		return request;
	}
	
	public static ResponseBean newInstance(String key, SocketAction action, Integer c, String msg) {
		ResponseBean bean = new ResponseBean(key, action);
		bean.setCodeMsg(c, msg);
		return bean;
	}

	
	
}
