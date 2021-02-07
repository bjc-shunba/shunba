package com.baidu.shunba.socket.service;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baidu.shunba.biz.entity.BDSerialNumber;
import com.baidu.shunba.biz.entity.SBDeviceEntity;
import com.baidu.shunba.biz.entity.SBDeviceLogEntity;
import com.baidu.shunba.common.utils.ObjectUtils;
import com.baidu.shunba.core.persistence.base.CriteriaQuery;
import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.server.log.SBLogUtils;
import com.baidu.shunba.server.serail.SerialUtils;
import com.baidu.shunba.socket.bean.RequestBean;
import com.baidu.shunba.socket.bean.ResponseBean;
import com.baidu.shunba.socket.bean.SocketAction;
import com.baidu.shunba.socket.bean.req.AuthBackRequest;
import com.baidu.shunba.socket.bean.req.SignRequest;
import com.baidu.shunba.socket.bean.resp.PushAuthResponse;

public class SocketActionHandler {

	public static ResponseBean handleAction(SystemService systemService, final RequestBean request) {
		ResponseBean rep;
		
		try {
			Method method = SocketActionHandler.class.getMethod(request.getAction().name(), SystemService.class, RequestBean.class);
		
			if (method == null) {
				rep = ResponseBean.newInstance(request.key, request.getAction(), -1, "不存在的方法");
			} else {
				rep = (ResponseBean) method.invoke(null, systemService, request);
			}
		} catch (Exception e) {
			rep = new ResponseBean(request.key, request.getAction());
			e.printStackTrace();
			rep.c = -1;
			rep.msg = e.getLocalizedMessage();
		}
		
		return rep;
	}
	
	public static ResponseBean sign(SystemService systemService, final RequestBean request) {
		ResponseBean rep = new ResponseBean(request.key, request.getAction());
		
		SignRequest signReq = request.getContent(SignRequest.class);
		
		SBDeviceEntity device = systemService.findUniqueByProperty(SBDeviceEntity.class, "deviceId", signReq.deviceId);
		if (device == null) {
			device = new SBDeviceEntity();
			device.setDeviceId(signReq.deviceId);
			rep.setCodeMsg(0, "设备尚未配置");
		} else {
			int delFlag = ObjectUtils.getIntValue(device.getDelFlag());
			if (delFlag == 1) {
				rep.setCodeMsg(-99, "设备已被删除，请联系管理员");
			} else {
				rep.setCodeMsg(1, "");
			}
		}
		
		device.setOsVersion(signReq.osVersion);
		device.setSpace(signReq.space);
		
		device.setIsOnline(1);
		device.setLastLogonTime(new Date());
		systemService.saveOrUpdate(device);
		
		return rep;
	}
	
	public static ResponseBean authBack(SystemService systemService, final RequestBean request) {
		ResponseBean rep = new ResponseBean(request.key, request.getAction());
		
		AuthBackRequest authBack = request.getContent(AuthBackRequest.class);
		
		BDSerialNumber serial = systemService.findUniqueByProperty(BDSerialNumber.class, "serialNumber", authBack.serialNumber);
		if (!authBack.deviceId.equals(serial.getDeviceId())) {
			SBLogUtils.log(systemService, authBack.deviceId, "序列号:" + serial.getSerialNumber() + ",所绑定的deviceId(" + serial.getDeviceId() + ")与上传deviceId(" + authBack.deviceId + ")不一致");
			
			//
			serial.setDeviceId(authBack.deviceId);
		}
		
		final Date now = new Date();
		
		if (authBack.success == 1) {
			serial.setIsUsed(1);
			serial.setUsedTime(now);
			
			systemService.updateEntitie(serial);
		} else {
			serial.setDelFlag(1);
			serial.setReason(authBack.reason);
			
			systemService.updateEntitie(serial);
			
			SerialUtils.autoSendSerial(systemService, authBack.deviceId);
		}
		
		return rep;
	}
	
	public static ResponseBean log(SystemService systemService, final RequestBean request) {
		ResponseBean rep = new ResponseBean(request.key, request.getAction());
		
		SBDeviceLogEntity log = request.getContent(SBDeviceLogEntity.class);
		systemService.save(log);
		
		return rep;
	}
	
	public static ResponseBean heart(SystemService systemService, final RequestBean request) {
		ResponseBean rep = new ResponseBean(request.key, request.getAction());
		return rep;
	}
	
}
