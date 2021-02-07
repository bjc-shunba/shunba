package com.baidu.shunba.core.persistence.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.baidu.shunba.core.persistence.base.CriteriaQuery;
import com.baidu.shunba.core.persistence.base.HqlQuery;
import com.baidu.shunba.core.vo.Autocomplete;
import com.baidu.shunba.core.vo.DBTable;
import com.baidu.shunba.core.vo.DataGridReturn;
import com.baidu.shunba.core.vo.DataTableReturn;
import com.baidu.shunba.core.vo.PageList;

public interface CommonService {

	/**
	 * 根据实体名返回全部对象
	 *
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 *
	 *         使用占位符的方式填充值 请注意：like对应的值格式："%"+username+"%" Hibernate Query
	 *
	 * @param hibernateTemplate
	 * @param hql
	 * @param valus             占位符号?对应的值，顺序必须一一对应 可以为空对象数组，但是不能为null
	 * @return 2008-07-19 add by liuyang
	 */
	public <T> List<T> executeQuery(final String hql, final Object[] values);

	public <T> List<T> executeQuery(final String hql, final Object[] values, int offset, int maxResults);

	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName();

	public Integer getAllDbTableSize();
	
	public Long getRowCount(CriteriaQuery cq);

	public void evict(Object object);

	public <T> Serializable save(T entity);
	public <T> Serializable forceSave(T entity);

	public <T> void saveOrUpdate(T entity);

	public <T> void delete(T entity);

	public <T> void batchSave(Collection<T> entitys);
	public <T> void forceBatchSave(Collection<T> entitys);

	public <T> void batchSaveOrUpdate(Collection<T> entitys);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T get(Class<T> class1, Serializable id);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T getEntity(Class entityName, Serializable id);

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);

	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

	public <T> T findUniqueByCriteriaQuery(CriteriaQuery cq);

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> loadAll(final Class<T> entityClass);

	/**
	 * 删除实体主键删除
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteEntityById(Class entityName, Serializable id);

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteAllEntitie(Collection<T> entities);

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(T pojo);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findByQueryString(String hql);

	/**
	 * 根据sql更新
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySqlString(String sql);

	/**
	 * 根据sql查找List
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findListbySql(String query);

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc);

	public <T> List<T> getList(Class clas);

	public <T> T singleResult(String hql);

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset);

	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq, final boolean isOffset);

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq, final boolean isOffset);

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final HqlQuery hqlQuery);

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageListBySql(final HqlQuery hqlQuery, final boolean isToEntity);

	public Session getSession();

	public List findByExample(final String entityName, final Object exampleEntity);

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage);

	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getAutoList(Autocomplete autocomplete);

	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, List<Object> param);

	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, Object... param);

	/**
	 * 执行SQL 使用:name占位符
	 */
	public Integer executeSql(String sql, Map<String, Object> param);

	/**
	 * 执行SQL 使用:name占位符,并返回执行后的主键值
	 */
	public Object executeSqlReturnKey(String sql, Map<String, Object> param);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public Map<String, Object> findOneForJdbc(String sql, Object... objs);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC
	 */
	public Long getCountForJdbc(String sql);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 * 
	 */
	public Long getCountForJdbcParam(String sql, Object[] objs);

	/**
	 * 查询单个基本对象，例如 queryForSingleObjectJdbc(sql, args, Double.class);
	 * 
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 */
	public <T> T queryForSingleObjectJdbc(String sql, Object[] args, Class<T> requiredType);

	/**
	 * 查询对象列表，例如 queryForObjectListJdbc(sql, args, Test.class);
	 * 
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 */
	public <T> List<T> queryForObjectListJdbc(String sql, Object[] args, Class<T> requiredType);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findHqlWithEnableQueryCache(String hql);

	public <T> List<T> findHql(String hql, Object... param);

	public <T> List<T> findPagedHql(String hql, int page, int rows, Object... param);

	public <T> List<T> findPagedHqlNew(String hql, int page, int rows, Map<String, Object> params);

	public <T> List<T> findSql(String sql, Object... param);

	public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult);

	public <T> List<T> findByDetached(DetachedCriteria dc);

	/**
	 * 执行存储过程
	 * 
	 * @param executeSql
	 * @param params
	 * @return
	 */
	public <T> List<T> executeProcedure(String procedureSql, Object... params);

	public int executeUpdateDelete(final String hql, final Object[] values);

	public int[] batchUpdateWithSql(String sql, List<Object[]> batchArgs);
}