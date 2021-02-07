package com.baidu.shunba.socket.service;

import com.baidu.shunba.core.persistence.service.SystemService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SystemService systemService;
	
	public ServerInitializer(SystemService systemService) {
		this.systemService = systemService;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("readTimeout", new ReadTimeoutHandler(90));

		// 以("\n")为结尾分割的 解码器
//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
//                Delimiters.lineDelimiter()));
//        ByteBuf delimiter[]= new ByteBuf[] {Unpooled.wrappedBuffer(new byte[] { '#', '#', '#' })};

		ByteBuf delimiter = Unpooled.copiedBuffer("###".getBytes());
		// 分割器,最大8K字节，超过将会报TooLongFrameException
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(32768, delimiter)); // 8192*4

		// 字符串解码 和 编码
		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

		// 自己的逻辑Handler
		pipeline.addLast("handler", new ServerHandler(systemService));
	}
}