package com.baidu.shunba.fwk.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import com.baidu.shunba.common.utils.oConvertUtils;

/**
 * Hiberate拦截器：实现创建人，创建时间，创建人名称自动注入;
 *                修改人, 修改时间, 修改人名自动注入;
 */
@Component
public class HiberAspect extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;

	private static final List<String> CreateDateFileds = new ArrayList<String>() {
		private static final long serialVersionUID = -3628016799548483192L;
		{
			add(DataBaseConstant.CREATE_DATE.toLowerCase());
			add(DataBaseConstant.CREATE_DATE_TABLE.toLowerCase());
			add(DataBaseConstant.CREATE_TIME.toLowerCase());
			add(DataBaseConstant.CREATE_TIME_TABLE.toLowerCase());
		}
	};
	
	private static final List<String> UpdateDateFileds = new ArrayList<String>() {
		private static final long serialVersionUID = -3970486822535271256L;

		{
			add(DataBaseConstant.UPDATE_DATE.toLowerCase());
			add(DataBaseConstant.UPDATE_DATE_TABLE.toLowerCase());
			add(DataBaseConstant.UPDATE_TIME.toLowerCase());
			add(DataBaseConstant.UPDATE_TIME_TABLE.toLowerCase());
		}
	};
	
	
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			// 添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				/* 找到名为"创建时间"的属性 */
				if (CreateDateFileds.contains(propertyNames[index].toLowerCase())) {
					/* 使用拦截器将对象的"创建时间"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				}
				else if (UpdateDateFileds.contains(propertyNames[index].toLowerCase())) {
					/* 使用拦截器将对象的"更新时间"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		// 添加数据
		for (int index = 0; index < propertyNames.length; index++) {
			/* 找到名为"修改时间"的属性 */
			if (UpdateDateFileds.contains(propertyNames[index].toLowerCase())) {
				/* 使用拦截器将对象的"修改时间"属性赋上值 */
				currentState[index] = new Date();
				continue;
			}
		}
		return true;
	}
}