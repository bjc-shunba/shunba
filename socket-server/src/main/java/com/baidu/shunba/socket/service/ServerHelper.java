package com.baidu.shunba.socket.service;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.shunba.biz.entity.SBDeviceEntity;
import com.baidu.shunba.biz.entity.SBShiftEntity;
import com.baidu.shunba.common.utils.ObjectUtils;
import com.baidu.shunba.common.utils.SUSequenceUtils;
import com.baidu.shunba.common.utils.StringUtils;
import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.core.web.bean.MessageJson;
import com.baidu.shunba.socket.bean.ResponseBean;
import com.baidu.shunba.socket.bean.SocketAction;
import com.baidu.shunba.socket.bean.resp.PushAuthResponse;

import io.netty.channel.ChannelHandlerContext;

public class ServerHelper {

    private static final Logger logger = LoggerFactory.getLogger(ServerHelper.class);

    public static MessageJson sendMessageToOne(SystemService systemService, String deviceId, ResponseBean message) {
        try {
            logger.info("SocketSever接收到推送到指定设备[" + deviceId + "]的消息: " + message);
            ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);
            if (context == null) {
                logger.info("SocketSever接收到推送到指定设备[" + deviceId + "]的消息，未找到设备: " + message);
                return MessageJson.newInstanceWithError("未找到连接设备");
            }

            context.writeAndFlush(message + ServerHandler.ResponseSplit);
            logger.info("SocketSever接收到推送到指定设备[" + deviceId + "]的消息已完成: " + message);
        } catch (Exception e) {
            logger.error("sendMessageToShift", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }


    public static MessageJson sendMessageToAll(SystemService systemService, ResponseBean message) {
        try {
            logger.info("SocketSever接收到推送到所有的消息: " + message);
            for (ChannelHandlerContext context : ServerHandler.onlineContexts.values()) {
                if (context != null) {
                    context.writeAndFlush(message + ServerHandler.ResponseSplit);
                }
            }
            logger.info("SocketSever接收到推送到所有的消息，完成推送: " + message);
        } catch (Exception e) {
            logger.error("sendMessageToShift", e);
            return MessageJson.newInstanceWithError(e);
        }

        return MessageJson.newInstance("ok");
    }


    public static MessageJson sendMessageToShift(SystemService systemService, String shiftNo, ResponseBean message) {
        try {
            logger.info("SocketSever接收到班次: [" + shiftNo + "]的新任务: " + message);
            List<Map<String, Object>> results = systemService.findForJdbc("select device_id as deviceId from sb_device where shift_no = '" + shiftNo + "'");
            if (results.size() == 0) {
                logger.error("SocketSever根据班次: [" + shiftNo + "]未查到已发车设备, 无法发送消息: " + message);
            }
            for (Map<String, Object> map : results) {
                String deviceId = ObjectUtils.getString(map.get("deviceId"));

                ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);
                if (context != null) {
                    logger.info("SocketSever根据班次: [" + shiftNo + "]查到已发车设备: [" + deviceId + "], 推送消息，开始，: " + message);
                    context.writeAndFlush(message + ServerHandler.ResponseSplit);
                    logger.info("SocketSever根据班次: [" + shiftNo + "]查到已发车设备: [" + deviceId + "], 推送消息，完成，" + message);
                }
            }
            logger.info("SocketSever接收到班次: [" + shiftNo + "]的新任务已完成: " + message);
        } catch (Exception e) {
            logger.error("sendMessageToShift", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }

    public static MessageJson sendMessageToLine(SystemService systemService, String lineId, ResponseBean message) {
        try {
            List<Map<String, Object>> results = systemService.findForJdbc("select device_id as deviceId from sb_device where line_id = '" + lineId + "'");
            logger.info("SocketSever接收到线路: [" + lineId + "]的票据更新任务，数据库返回设备数：[" + results.size() + "]: " + message);
            for (Map<String, Object> map : results) {
                String deviceId = ObjectUtils.getString(map.get("deviceId"));
                ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);
                if (context != null) {
                    logger.info("SocketSever根据线路: [" + lineId + "]查到已发车设备: [" + deviceId + "], 推送消息，开始，: " + message);
                    context.writeAndFlush(message + ServerHandler.ResponseSplit);
                    logger.info("SocketSever根据线路: [" + lineId + "]查到已发车设备: [" + deviceId + "], 推送消息，开始，: " + message);
                }
            }
            logger.info("SocketSever接收到线路: [" + lineId + "]的票据更新任务，已完成: " + message);
        } catch (Exception e) {
            logger.error("sendMessageToLine", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }

    //因为auth可能有多个地方，需要处理
    public static MessageJson pushAuthToDevice(SystemService systemService, String deviceId) {
        try {
            ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);
            if (context == null) {
                return MessageJson.newInstanceWithError("未找到连接设备");
            }

            SBDeviceEntity device = systemService.findUniqueByProperty(SBDeviceEntity.class, "deviceId", deviceId);

            if (device == null) {
                ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.pushInvalid);
                response.setCodeMsg(0, "未找到设备:" + deviceId);
                context.writeAndFlush(response + ServerHandler.ResponseSplit);
                return MessageJson.newInstanceWithError("未找到设备:" + deviceId);
            }

            pushAuthToDevice(systemService, device);
        } catch (Exception e) {
            logger.error("pushAuthToDevice", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }

    //因为auth可能有多个地方，需要处理
    public static MessageJson pushAuthToDevice(SystemService systemService, SBDeviceEntity device) {
        try {
            ChannelHandlerContext context = ServerHandler.onlineContexts.get(device.getDeviceId());
            if (context == null) {
                return MessageJson.newInstanceWithError("未找到连接设备");
            }

            if (ObjectUtils.getIntValue(device.getDelFlag()) > 0) {
                ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.pushInvalid);
                response.setCodeMsg(1, "设备已删除");
                context.writeAndFlush(response + ServerHandler.ResponseSplit);
                return MessageJson.newInstanceWithError("设备已删除");
            }
            if (StringUtils.isEmpty(device.getSerialNumberId())) {
                ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.pushAuth);
                response.setCodeMsg(0, "设备未配置");
                context.writeAndFlush(response + ServerHandler.ResponseSplit);
                return MessageJson.newInstanceWithError("设备未配置");
            }

            ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.pushAuth);

            PushAuthResponse content = new PushAuthResponse();
            content.lineId = device.getLineId();
            content.shiftNo = device.getShiftNo();
            try {
                SBShiftEntity shfit = systemService.findUniqueByProperty(SBShiftEntity.class, "shiftNo", device.getShiftNo());
                content.shiftName = shfit.getName();
            } catch (Exception e) {
                logger.error("pushAuthToDevice", e);
            }
            content.serialNumber = device.getSerialNumber().getSerialNumber();
            response.setContent(content);

            logger.info("SocketSever推送注册信息到设备[" + device.getDeviceId() + "]，开始: " + response);
            context.writeAndFlush(response + ServerHandler.ResponseSplit);
            logger.info("SocketSever推送注册信息到设备[" + device.getDeviceId() + "]，完成: " + response);
        } catch (Exception e) {
            logger.error("pushAuthToDevice", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }


    public static MessageJson pushInvalidToDevice(SystemService systemService, String deviceId) {
        try {
            ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);
            if (context == null) {
                return MessageJson.newInstanceWithError("未找到连接设备");
            }

            ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.pushInvalid);
            response.setCodeMsg(1, "设备已禁用");
            context.writeAndFlush(response + ServerHandler.ResponseSplit);
        } catch (Exception e) {
            logger.error("pushInvalidToDevice", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }

    /**
     * 向指定设备发送初始化信号
     *
     * @param deviceId
     * @return
     */
    public static MessageJson sendInit(String deviceId) {
        try {
            ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);
            if (context == null) {
                return MessageJson.newInstanceWithError("未找到连接设备");
            }

            ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.init);
            logger.info(" ========== 启动向设备 [" + deviceId + "]发送初始化信号...");
            context.writeAndFlush(response + ServerHandler.ResponseSplit);
            logger.info(" ========== 向设备 [" + deviceId + "]发送初始化信号成功...");
        } catch (Exception e) {
            logger.error(" ========== 向设备 [" + deviceId + "]发送初始化信号失败, 失败原因: ", e);
            return MessageJson.newInstanceWithError(e);
        }
        return MessageJson.newInstance("ok");
    }

    /**
     * 向所有设备发送初始化信号
     *
     * @return
     */
    public static MessageJson sendInitAll() {
        ResponseBean response = new ResponseBean(SUSequenceUtils.generateNumberCharSequence(), SocketAction.init);

        for (String deviceId : ServerHandler.onlineContexts.keySet()) {
            ChannelHandlerContext context = ServerHandler.onlineContexts.get(deviceId);

            if (context != null) {
                try {

                    logger.info(" ========== 启动向设备 [" + deviceId + "]发送初始化信号...");

                    context.writeAndFlush(response + ServerHandler.ResponseSplit);
                    logger.info(" ========== 向设备 [" + deviceId + "]发送初始化信号成功...");
                } catch (Exception e) {
                    logger.error(" ========== 向设备 [" + deviceId + "]发送初始化信号失败, 失败原因: ", e);
                    return MessageJson.newInstanceWithError(e);
                }
            }
        }

        return MessageJson.newInstance("ok");
    }

}
