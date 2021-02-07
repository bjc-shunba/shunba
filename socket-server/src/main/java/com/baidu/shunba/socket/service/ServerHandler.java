package com.baidu.shunba.socket.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.shunba.biz.entity.SBDeviceEntity;
import com.baidu.shunba.common.gson.GsonUtils;
import com.baidu.shunba.common.utils.ObjectUtils;
import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.core.web.bean.MessageJson;
import com.baidu.shunba.socket.bean.RequestBean;
import com.baidu.shunba.socket.bean.ResponseBean;
import com.baidu.shunba.socket.bean.SocketAction;
import com.baidu.shunba.socket.bean.req.SignRequest;
import com.baidu.shunba.socket.bean.resp.PushAuthResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static Logger logger = Logger.getLogger("com.mxzhang.service.ServerHandler");

    public static final String ResponseSplit = "###";

    protected static final ConcurrentHashMap<String, ChannelHandlerContext> onlineContexts = new ConcurrentHashMap<>();


    private final SystemService systemService;

    public ServerHandler(SystemService systemService) {
        this.systemService = systemService;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            System.out.println((new Date()).toString() + ctx.channel().remoteAddress() + " timeout.");
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 收到消息直接打印输出
//        System.out.println((new Date()).toString() + ctx.channel().remoteAddress() + " Say : " + msg);

        // 根据传入的方法名找到相应的方法
        RequestBean request = null;
        try {
            request = GsonUtils.fromJson(msg, RequestBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!checkBeforHandler(ctx, request)) {
            return;
        }

        ResponseBean response;
        if (request.getAction().needProcess) {
            response = SocketActionHandler.handleAction(systemService, request);
        } else {
            response = ResponseBean.newInstance(request.key, request.getAction(), 0, request.getAction().memo);
        }


        response.t = System.currentTimeMillis();

//		System.out.println("Tell: " + ctx.channel().remoteAddress() + ", content: " + response.toJson());
        // 回写结果
        ctx.writeAndFlush(response + ResponseSplit);
        // 如果是转发操作关闭连接

        if (request.getAction().needClose) {
            ctx.close();
        } else {
            if (request.getAction() == SocketAction.sign && !request.getAction().needClose) {
                SignRequest reqContent = request.getContent(SignRequest.class);
                //add to cache
                if (response.isSuccess()) {
                    onlineContexts.put(reqContent.deviceId, ctx);

                    ServerHelper.pushAuthToDevice(systemService, reqContent.deviceId);
                } else {
                    if (response.c == -99) {
                        ServerHelper.pushInvalidToDevice(systemService, reqContent.deviceId);
                    }
                }
            }
        }
    }

    private static boolean checkBeforHandler(ChannelHandlerContext ctx, RequestBean request) {
        if (request == null) {
            logger.info("非法报文");
            ResponseBean response = ResponseBean.newInstance("null", SocketAction.unknow, 0, "非法报文");

            ctx.writeAndFlush(GsonUtils.toJson(response) + ResponseSplit);
            ctx.close();

            return false;
        }

        if (request.getAction() == SocketAction.sign) {
            logger.info("========== 执行设备注册: " + new Gson().toJson(request));
            SignRequest reqContent = request.getContent(SignRequest.class);
            if (reqContent == null) {
                ResponseBean response = ResponseBean.newInstance(request.key, request.getAction(), 0, "非法参数!");
                ctx.writeAndFlush(GsonUtils.toJson(response) + ResponseSplit);
                return false;
            } else if (onlineContexts.keySet().contains(reqContent.deviceId)) {
                ChannelHandlerContext oldCtx = onlineContexts.get(reqContent.deviceId);
                if (ctx == oldCtx) {
                    ResponseBean response = ResponseBean.newInstance(request.key, request.getAction(), 0, "不要重复注册设备");
                    ctx.writeAndFlush(GsonUtils.toJson(response) + ResponseSplit);
                    return false;
                } else {
                    ResponseBean response = ResponseBean.newInstance(request.key, request.getAction(), 0, "该ID已在其他设备登陆，本机已强制下线");
                    oldCtx.writeAndFlush(GsonUtils.toJson(response) + ResponseSplit);
                    oldCtx.close();
                    return true;
                }
            }
        }

        return true;
    }

    /*
     *
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     *
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

//        ctx.writeAndFlush("Welcome to "
//                + InetAddress.getLocalHost().getHostName() + " service!\n");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // remove context from RAM
        System.out.println((new Date()).toString() + "RemoteAddress : " + ctx.channel().remoteAddress() + " inactive !");
        // 移除断开的ctx
        for (String key : onlineContexts.keySet()) {
            if (ctx == onlineContexts.get(key)) {
                onlineContexts.remove(key);
                // 更新设备在线状态
                System.out.println("inavtive 0: find key = " + key);
                SBDeviceEntity device = systemService.findUniqueByProperty(SBDeviceEntity.class, "deviceId", key);
                device.setIsOnline(0);
                device.setLastLogoffTime(new Date());
                systemService.saveOrUpdate(device);
                System.out.println("inavtive commit 1: remove key = " + key);
            }
        }
        System.out.println("inavtive: finish clear");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered ");
    }
}
