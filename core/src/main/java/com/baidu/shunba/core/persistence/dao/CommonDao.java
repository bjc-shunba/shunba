package com.baidu.shunba.core.persistence.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.hibernate.Query;
import org.hibernate.sql.Template;
import org.springframework.stereotype.Repository;

import com.baidu.shunba.common.utils.DateUtils;
import com.baidu.shunba.common.utils.PasswordUtil;
import com.baidu.shunba.core.entity.TSRoleUser;
import com.baidu.shunba.core.entity.TSUser;

/**
 * 公共扩展方法
 */
@Repository
public class CommonDao extends GenericBaseCommonDao implements ICommonDao, IGenericBaseCommonDao {

	@Override
	public TSUser getUserByUserIdAndUserNameExits(TSUser user) {
		String query = "from TSUser u where u.userName = :username and u.password=:passowrd";
		Query queryObject = getSession().createQuery(query);
		String pwd = PasswordUtil.encrypt(user.getUserName(), user.getPassword(), PasswordUtil.getStaticSalt());
		queryObject.setParameter("username", user.getUserName());
		queryObject.setParameter("passowrd", pwd);
		List<TSUser> users = queryObject.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	public TSUser getUserByUserIdAndUserNameExits(String userName, String password) {
		String query = "from TSUser u where u.userName = :username and u.password=:passowrd";
		Query queryObject = getSession().createQuery(query);
		queryObject.setParameter("username", userName);
		queryObject.setParameter("passowrd", password);
		List<TSUser> users = queryObject.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	public String getUserRole(TSUser user) {
		String userRole = "";
		List<TSRoleUser> sRoleUser = findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		for (TSRoleUser tsRoleUser : sRoleUser) {
			userRole += tsRoleUser.getTSRole().getRoleCode() + ",";
		}
		return userRole;
	}

	
}
