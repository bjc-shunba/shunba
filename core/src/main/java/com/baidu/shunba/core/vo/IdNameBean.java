package com.baidu.shunba.core.vo;

import java.io.Serializable;

public class IdNameBean implements Serializable {

	private static final long serialVersionUID = 8287928978945266810L;

	public Object id;
	public String name;

	public static IdNameBean newInstance(Object id, String name) {
		IdNameBean object = new IdNameBean();
		object.id = id;
		object.name = name;
		return object;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}