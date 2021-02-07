package com.baidu.shunba.server.serail;

import java.util.List;
import java.util.Random;

import com.baidu.shunba.biz.entity.BDSerialNumber;
import com.baidu.shunba.biz.entity.SBDeviceEntity;
import com.baidu.shunba.core.persistence.base.CriteriaQuery;
import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.core.vo.SortDirection;
import com.baidu.shunba.server.log.SBLogUtils;
import com.baidu.shunba.socket.service.ServerHelper;

public class SerialUtils {

	public static BDSerialNumber getValidSerial(SystemService systemService) {
		CriteriaQuery cq = new CriteriaQuery(BDSerialNumber.class);
		cq.notEq("isUsed", 1);
		cq.notEq("delFlag", 1);
		cq.isNull("deviceId");
		cq.setCurPage(1);
		cq.setPageSize(10);
		cq.addOrder("updateDate", SortDirection.desc);
		cq.add();
		
		List<BDSerialNumber> datas = systemService.getListByCriteriaQuery(cq, true);
		
		Random radom = new Random(System.currentTimeMillis());
		if (datas != null && datas.size() > 0) {
			return datas.get(radom.nextInt(datas.size()));
		}
		return null;
	}
	
	
	public static void autoSendSerial(SystemService systemService, String deviceId) {
		SBDeviceEntity device = systemService.findUniqueByProperty(SBDeviceEntity.class, "deviceId", deviceId);
		
		BDSerialNumber serial = getValidSerial(systemService);
		
		if (device == null) {
			SBLogUtils.log(systemService, deviceId, "自动更换序列号时，查询设备为空：" + deviceId);
			return;
		}
		
		if (serial == null) {
			SBLogUtils.log(systemService, deviceId, "警告！自动更换序列号时，查询不到新的序列号");
			return;
		}
		
		serial.setDeviceId(deviceId);
		device.setSerialNumberId(serial.getId());
		
		systemService.updateEntitie(serial);
		systemService.updateEntitie(device);
		
		ServerHelper.pushAuthToDevice(systemService, deviceId);
	}
	
	
	
}
