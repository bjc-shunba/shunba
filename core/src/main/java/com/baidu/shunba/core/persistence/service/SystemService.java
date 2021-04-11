package com.baidu.shunba.core.persistence.service;

import java.util.List;
import java.util.Set;




/**
 * SystemService接口，继承自CommonService
 */
public interface SystemService extends CommonService {
	/**
 	 * 方法描述:  查询数据字典
 	 * 日    期： 2014年5月11日-下午4:22:42
 	 * @param dicTable
 	 * @param dicCode
 	 * @param dicText
 	 * @return
 	 * 返回类型： List<DictEntity>
 	 */
// 	public List<DictEntity> queryDict(String dicTable,String dicCode, String dicText);


	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param loglevel 级别
	 * @param operatetype 类型
	 */
	public void addLog(String LogContent, Integer loglevel, Integer operatetype);
	
	/**
	 * 日志添加
	 * @param username 操作人
	 * @param logcontent 内容
	 * @param loglevel 级别
	 * @param operatetype 类型
	 */
	public void addLogForApi(String username, String logcontent, Integer loglevel, Integer operatetype);
	

	/**
	 * 添加数据日志
	 * @param tableName		操作表名
	 * @param dataId		数据ID
	 * @param dataContent	内容(JSON格式)
	 */
	public void addDataLog(String tableName, String dataId, String dataContent);
}