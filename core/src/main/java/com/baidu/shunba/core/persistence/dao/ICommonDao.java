package com.baidu.shunba.core.persistence.dao;

import java.util.Map;

import org.hibernate.sql.Template;

import com.baidu.shunba.core.entity.TSUser;

public interface ICommonDao extends IGenericBaseCommonDao {

	/**
	 * 检查用户是否存在
	 */
	public TSUser getUserByUserIdAndUserNameExits(TSUser user);

	public TSUser getUserByUserIdAndUserNameExits(String userName, String password);

	public String getUserRole(TSUser user);

	

}
