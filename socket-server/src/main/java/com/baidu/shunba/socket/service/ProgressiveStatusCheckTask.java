package com.baidu.shunba.socket.service;

import java.util.Date;
import java.util.TimerTask;

public class ProgressiveStatusCheckTask extends TimerTask {
	public void run() {
		System.out.println("-------设备检查任务执行开始:"+(new Date())+"--------");  
        long s = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		for (String key : ServerHandler.onlineContexts.keySet()) {
    		sb.append(key).append(":").append(ServerHandler.onlineContexts.get(key).channel().remoteAddress()).append("\n");
//    		ChannelHandlerContext posCtx = ServerHandler.ctxMap.get(key);
    		
//    		ChannelHandlerContext lastHeartTime = ServerHandler.ctxMap.get(key);
//    		if(System.currentTimeMillis()-lastHeartTime.longValue()>180000){	//超过3分钟没有发过任何包来，表示已经断了
//    			System.out.println("主动检测到设备超过3分钟没有任何包发过来，关掉此设备的连接，UDID = "+key);
//    			try {
//    				posCtx.close(); //主动关掉此连接
//        			
//        			//更新设备在线状态
//         			BaseDAO dao = new BaseDAO();
//         			DeviceEntity pos = dao.findUniqueByProperty(DeviceEntity.class, "udid", key);
//         			pos.setOnLineStatus(0);
//         			dao.saveOrUpdate(pos);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//    			ServerHandler.ctxMap.remove(key);
//    		}else{
//    			String pushContent = "{\"m\":\"preProHeart\",\"code\":1,\"msg\":\"ok\"}" + "###";
//    			posCtx.writeAndFlush(pushContent);
//    		}
    		
		}
//		System.out.println(sb.toString());
    	System.out.println("---设备检查任务执行完成, 设备数量："+ServerHandler.onlineContexts.keySet().size()+"，耗时："+(System.currentTimeMillis()-s)+"ms---");  
    }
}