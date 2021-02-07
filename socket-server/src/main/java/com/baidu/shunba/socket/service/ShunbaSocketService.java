package com.baidu.shunba.socket.service;

import com.baidu.shunba.core.persistence.service.SystemService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ShunbaSocketService {

	/**
	 * 服务端监听的端口地址
	 */
	public static final int portNumber = 7890;

	public static void start(final SystemService systemService) throws InterruptedException {
		//1
		try {
			systemService.executeSql("update sb_device set is_online = 0");
		} catch (Exception e) {
		}
		
		//2
//		Timer timer = new Timer();
//		timer.schedule(new ProgressiveStatusCheckTask(), 10000, 90000);    // delay 10秒 后执行该任务, 每90秒执行一次

		//3
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ServerInitializer(systemService));

			// 服务器绑定端口监听
			ChannelFuture f = b.bind(portNumber).sync();

			// 监听服务器关闭监听
			f.channel().closeFuture().sync();

			// 可以简写为
			/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}