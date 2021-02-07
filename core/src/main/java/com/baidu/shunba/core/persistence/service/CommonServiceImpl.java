package com.baidu.shunba.core.persistence.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.shunba.common.utils.StringUtil;
import com.baidu.shunba.core.persistence.base.CriteriaQuery;
import com.baidu.shunba.core.persistence.base.HqlQuery;
import com.baidu.shunba.core.persistence.dao.ICommonDao;
import com.baidu.shunba.core.vo.Autocomplete;
import com.baidu.shunba.core.vo.DBTable;
import com.baidu.shunba.core.vo.DataGridReturn;
import com.baidu.shunba.core.vo.DataTableReturn;
import com.baidu.shunba.core.vo.PageList;

@Service("commonService")
@Transactional("transactionManagerMysql")
public class CommonServiceImpl implements CommonService {
	
	public ICommonDao commonDao = null;

	public <T> List<T> executeQuery(final String hql, final Object[] values) {
		return commonDao.executeQuery(hql, values);
	}

	public <T> List<T> executeQuery(final String hql, final Object[] values, int offset, int maxResults) {
		return commonDao.executeQuery(hql, values, offset, maxResults);
	}

	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		return commonDao.getAllDbTableName();
	}

	public Integer getAllDbTableSize() {
		return commonDao.getAllDbTableSize();
	}
	
	public Long getRowCount(CriteriaQuery cq) {
		return this.commonDao.getRowCount(cq);
	}

	public void evict(Object object) {
		this.commonDao.evict(object);
	}

	@Resource
	public void setCommonDao(ICommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public <T> Serializable save(T entity) {
		return commonDao.save(entity);
	}
	public <T> Serializable forceSave(T entity) {
		return commonDao.forceSave(entity);
	}

	public <T> void saveOrUpdate(T entity) {
		commonDao.saveOrUpdate(entity);
	}

	public <T> void delete(T entity) {
		commonDao.delete(entity);
	}

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteAllEntitie(Collection<T> entities) {
		commonDao.deleteAllEntitie(entities);
	}

	/**
	 * 根据实体名获取对象
	 */
	public <T> T get(Class<T> class1, Serializable id) {
		return commonDao.get(class1, id);
	}

	/**
	 * 根据实体名返回全部对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 */
	public <T> List<T> getList(Class clas) {
		return commonDao.loadAll(clas);
	}

	/**
	 * 根据实体名获取对象
	 */
	public <T> T getEntity(Class entityName, Serializable id) {
		return commonDao.getEntity(entityName, id);
	}

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		return commonDao.findUniqueByProperty(entityClass, propertyName, value);
	}

	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {

		return commonDao.findByProperty(entityClass, propertyName, value);
	}

	public <T> T findUniqueByCriteriaQuery(CriteriaQuery cq) {
		return commonDao.findUniqueByCriteriaQuery(cq);
	}

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> loadAll(final Class<T> entityClass) {
		return commonDao.loadAll(entityClass);
	}

	public <T> T singleResult(String hql) {
		return commonDao.singleResult(hql);
	}

	/**
	 * 删除实体主键ID删除对象
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteEntityById(Class entityName, Serializable id) {
		commonDao.deleteEntityById(entityName, id);
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(T pojo) {
		commonDao.updateEntitie(pojo);

	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findByQueryString(String hql) {
		return commonDao.findByQueryString(hql);
	}

	/**
	 * 根据sql更新
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySqlString(String sql) {
		return commonDao.updateBySqlString(sql);
	}

	/**
	 * 根据sql查找List
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findListbySql(String query) {
		return commonDao.findListbySql(query);
	}

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc) {
		return commonDao.findByPropertyisOrder(entityClass, propertyName, value, isAsc);
	}

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.getPageList(cq, isOffset);
	}

	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.getDataTableReturn(cq, isOffset);
	}

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.getDataGridReturn(cq, isOffset);
	}

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final HqlQuery hqlQuery) {
		return commonDao.getPageList(hqlQuery);
	}

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageListBySql(final HqlQuery hqlQuery, final boolean isToEntity) {
		return commonDao.getPageListBySql(hqlQuery, isToEntity);
	}

	public Session getSession()

	{
		return commonDao.getSession();
	}

	public List findByExample(final String entityName, final Object exampleEntity) {
		return commonDao.findByExample(entityName, exampleEntity);
	}

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage) {
		return commonDao.getListByCriteriaQuery(cq, ispage);
	}

	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getAutoList(Autocomplete autocomplete) {
		StringBuffer sb = new StringBuffer("");
		for (String searchField : autocomplete.getSearchField().split(",")) {
			sb.append(" or (  " + searchField + " like '%" + autocomplete.getTrem().replace("'", "").replace("%", "") + "%' ");
			if (StringUtil.isNotEmpty(autocomplete.getExtend())) {
				sb.append(" and ");
				sb.append(autocomplete.getExtend());
				sb.append(" ) ");
			} else {
				sb.append(" ) ");
			}
		}
		String hql = "from " + autocomplete.getEntityName() + " where 1!=1 " + sb.toString();
		return commonDao.getSession().createQuery(hql).setFirstResult(autocomplete.getCurPage() - 1).setMaxResults(autocomplete.getMaxRows()).list();
	}

	public Integer executeSql(String sql, List<Object> param) {
		return commonDao.executeSql(sql, param);
	}

	public Integer executeSql(String sql, Object... param) {
		return commonDao.executeSql(sql, param);
	}

	public Integer executeSql(String sql, Map<String, Object> param) {
		return commonDao.executeSql(sql, param);
	}

	public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
		return commonDao.executeSqlReturnKey(sql, param);
	}

	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		return commonDao.findForJdbc(sql, page, rows);
	}

	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return commonDao.findForJdbc(sql, objs);
	}

	public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs) {
		return commonDao.findForJdbcParam(sql, page, rows, objs);
	}

	public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
		return commonDao.findObjForJdbc(sql, page, rows, clazz);
	}

	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		return commonDao.findOneForJdbc(sql, objs);
	}

	public Long getCountForJdbc(String sql) {
		return commonDao.getCountForJdbc(sql);
	}

	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return commonDao.getCountForJdbcParam(sql, objs);
	}

	public <T> T queryForSingleObjectJdbc(String sql, Object[] args, Class<T> requiredType) {
		return commonDao.queryForSingleObjectJdbc(sql, args, requiredType);
	}

	public <T> List<T> queryForObjectListJdbc(String sql, Object[] args, Class<T> requiredType) {
		return commonDao.queryForObjectListJdbc(sql, args, requiredType);
	}

	public <T> void batchSave(Collection<T> entitys) {
		this.commonDao.batchSave(entitys);
	}
	public <T> void forceBatchSave(Collection<T> entitys) {
		this.commonDao.forceBatchSave(entitys);
	}

	public <T> void batchSaveOrUpdate(Collection<T> entitys) {
		this.commonDao.batchSaveOrUpdate(entitys);
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findHqlWithEnableQueryCache(String hql) {
		return this.commonDao.findHqlWithEnableQueryCache(hql);
	}

	public <T> List<T> findHql(String hql, Object... param) {
		return this.commonDao.findHql(hql, param);
	}

	public <T> List<T> findPagedHql(String hql, int page, int rows, Object... param) {
		return this.commonDao.findPagedHql(hql, page, rows, param);
	}

	public <T> List<T> findPagedHqlNew(String hql, int page, int rows, Map<String, Object> params) {
		return this.commonDao.findPagedHqlNew(hql, page, rows, params);
	}

	public <T> List<T> findSql(String sql, Object... param) {
		return this.commonDao.findSql(sql, param);
	}

	public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult) {
		return this.commonDao.pageList(dc, firstResult, maxResult);
	}

	public <T> List<T> findByDetached(DetachedCriteria dc) {
		return this.commonDao.findByDetached(dc);
	}

	/**
	 * 调用存储过程
	 */
	public <T> List<T> executeProcedure(String procedureSql, Object... params) {
		return this.commonDao.executeProcedure(procedureSql, params);
	}

	public int executeUpdateDelete(final String hql, final Object[] values) {
		return this.commonDao.executeUpdateDelete(hql, values);
	}

	public int[] batchUpdateWithSql(String sql, List<Object[]> batchArgs) {
		return this.commonDao.batchUpdateWithSql(sql, batchArgs);
	}
}
