package com.baidu.shunba.core.vo;

import java.io.Serializable;
import java.util.List;

public class DBTable<T> implements Serializable {

	private static final long serialVersionUID = -8183560139512006259L;

	public String tableName;
	public String entityName;
	public String tableTitle;
	public Class<T> tableEntityClass;
	public List<T> tableData;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public Class<T> getClass1() {
		return tableEntityClass;
	}

	public void setClass1(Class<T> class1) {
		this.tableEntityClass = class1;
	}

	public List<T> getTableData() {
		return tableData;
	}

	public void setTableData(List<T> tableData) {
		this.tableData = tableData;
	}

}