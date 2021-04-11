package com.baidu.shunba.core.persistence.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baidu.shunba.common.utils.ContextHolderUtils;
import com.baidu.shunba.common.utils.ResourceUtil;
import com.baidu.shunba.common.utils.StringUtil;
import com.baidu.shunba.core.entity.TSDatalogEntity;
import com.baidu.shunba.core.entity.TSLog;
import com.baidu.shunba.core.persistence.base.CriteriaQuery;

@Service("systemService")
@Transactional
public class SystemServiceImpl extends CommonServiceImpl implements SystemService {

//	public List<DictEntity> queryDict(String dicTable, String dicCode,String dicText){
//		List<DictEntity> dictList = null;
//		//step.1 如果没有字典表则使用系统字典表
//		if(StringUtil.isEmpty(dicTable)){
//			dictList = jeecgDictDao.querySystemDict(dicCode);
//			for(DictEntity t:dictList){
//				t.setTypename(t.getTypename());
//			}
//		}else {
//			dicText = StringUtil.isEmpty(dicText, dicCode);
//			dictList = jeecgDictDao.queryCustomDict(dicTable, dicCode, dicText);
//		}
//		return dictList;
//	}
//	
//	public List<DictEntity> queryCustomDict(String dicTable, String dicCode,String dicText){
//		
//	}
//	
//	public List<DictEntity> querySystemDict(String dicCode){
//		
//	}

	/**
	 * 添加日志
	 */
	public void addLog(String logcontent, Integer loglevel, Integer operatetype) {
		if (logcontent != null && logcontent.length() > 1000) {
			logcontent = logcontent.substring(0, 1000);
		}
		HttpServletRequest request = ContextHolderUtils.getRequest();
		TSLog log = new TSLog();
		log.setLogcontent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperatetype(operatetype);
		log.setNote(getRemoteIp(request));
		log.setBroswer("");
		log.setOperatetime(new Date());

		commonDao.save(log);
	}

	/**
	 * 添加日志For API
	 */
	public void addLogForApi(String username, String logcontent, Integer loglevel, Integer operatetype) {
		if (logcontent != null && logcontent.length() > 1000) {
			logcontent = logcontent.substring(0, 1000);
		}
		HttpServletRequest request = ContextHolderUtils.getRequest();
//		String broswer = BrowserUtils.checkBrowse(request);
		TSLog log = new TSLog();
		log.setLogcontent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperatetype(operatetype);
		log.setNote(getRemoteIp(request));
//		log.setBroswer(broswer);
		log.setOperatetime(new Date());
		log.setUsername(username);
		commonDao.save(log);
	}
	
	private String getRemoteIp(HttpServletRequest request) {
		String ip = "127.0.0.1";
		if (request == null) {
			return ip;
		}
		try {
			ip = request.getHeader("X-Real-IP");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Forwarded-For");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} catch (Exception e) {
		}
		
		return ip;
	}

	@Override
	public void addDataLog(String tableName, String dataId, String dataContent) {

		int versionNumber = 0;

		Integer integer = commonDao.singleResult("select max(versionNumber) from TSDatalogEntity where tableName = '" + tableName + "' and dataId = '" + dataId + "'");
		if (integer != null) {
			versionNumber = integer.intValue();
		}

		TSDatalogEntity tsDatalogEntity = new TSDatalogEntity();
		tsDatalogEntity.setTableName(tableName);
		tsDatalogEntity.setDataId(dataId);
		tsDatalogEntity.setDataContent(dataContent);
		if (integer != null) {
			tsDatalogEntity.setVersionNumber(versionNumber + 1);
		}
		commonDao.save(tsDatalogEntity);
	}
}
