package com.baidu.shunba.socket.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.shunba.common.gson.GsonUtils;
import com.baidu.shunba.socket.bean.RequestBean;
import com.baidu.shunba.socket.bean.SocketAction;
import com.baidu.shunba.socket.bean.req.SignRequest;

public class TestNio {

	

    private static final String ServerName = "192.168.50.233";
    private static final int ServerPort = 7890;
    private static final String ResponseSplit = "###";

    private final static int DATA_MAX_LEN = 0xFFFF;
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static volatile boolean isShutDown = false;
    //private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static SocketChannel socketChannel;
    private static Selector selector;
    private static final BlockingQueue<byte[]> sendDataQueue = new ArrayBlockingQueue<>(100);
    private static final ByteBuffer requestDataBuffer = ByteBuffer.allocate(DATA_MAX_LEN);
    private static final ByteArrayOutputStream responseDataBuffer = new ByteArrayOutputStream(DATA_MAX_LEN);

    /**
     * 发送任务处理线程
     * @param data byte[]
     */
    public static void sendData(final byte[] data){
        if (data==null){
            return;
        }
        try{
            sendDataQueue.offer(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 1 启动连接及读数据监听线程；
     * 2 启动发数据监听线程；
     */
    public static synchronized void startService() {
        //启动及接收数据线程
        executor.execute(()->{
            try{
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                selector = Selector.open();
                SocketAddress socketAddress = new InetSocketAddress(ServerName, ServerPort);
                //连接服务端socket
                socketChannel.connect(socketAddress);
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                
                //selector处理
                while (!isShutDown) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    SocketChannel client;
                    while(keyIterator.hasNext()){
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();
                        
                        client = (SocketChannel) key.channel();
                        if (key.isAcceptable()){
//                          //PLog.d(TAG,"与服务端口["+serverIp+":"+port+"]连接成功");
//                      	SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
//                          //非阻塞
//                      	clientChannel.configureBlocking(false);
//                          //注册selector
//                      	clientChannel.register(key.selector(), SelectionKey.OP_READ);
//                          System.out.println("建立请求......");
                        } else if (key.isConnectable()){
                            //PLog.d(TAG,"与服务端口["+serverIp+":"+port+"]连接成功");
                            if(client.finishConnect()) {
                                client.configureBlocking(false);
                                client.register(selector, SelectionKey.OP_READ);
                                //countDownLatch.countDown();
                                beginWrite();
                            }
                        } else if (key.isReadable()) {
                        	//数据缓冲
                            ByteBuffer dataBuffer = ByteBuffer.allocate(DATA_MAX_LEN);
                        	dataBuffer.clear();
                        	
                        	int readLen = 0;
                        	try {
                        		readLen = client.read(dataBuffer);
							} catch (IOException e) {
								//TODO, 处理连接被关闭的情况，如果是ID被别人顶替了，界面需要提示。
								throw e;
							}
                            
                            
                            if (readLen <= 0) {
                            	Thread.sleep(200);
                            	continue;
                            }
                            
                            byte[] result = new byte[readLen];
                            dataBuffer.flip();
                            dataBuffer.get(result);
                            
                            responseDataBuffer.write(result);
                            
                            byte[] allResult = responseDataBuffer.toByteArray();
                            String value = new String(allResult, "utf-8");
                            
                            if (value.indexOf(ResponseSplit) > 0) {
                            	if (!value.endsWith(ResponseSplit)) {
                            		String has = value.substring(0, value.lastIndexOf(ResponseSplit)) + ResponseSplit;
                            		int hasParsed = has.getBytes().length;
                            		
                            		responseDataBuffer.reset();
                            		responseDataBuffer.write(result, hasParsed, result.length - hasParsed);
                            		
                            		value = has;
                            	} else {
                            		responseDataBuffer.reset();
                            	}
                            } else {
                            	value = "";
                            	responseDataBuffer.write(result);
                            }
                            
                            if (value.indexOf(ResponseSplit) > 0) {
                            	String[] response = value.split(ResponseSplit);
                            	
                            	for (String resp : response) {
									System.err.println("get msg ::" + resp);
								}
                            }
                            
                            //PLog.d(TAG, PUtils.toHexStr(result));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private static synchronized void beginWrite() {
    	executor.execute(()->{
            try {
                //countDownLatch.await();
                while (!isShutDown){
                    if (socketChannel != null && socketChannel.isConnected()){
                        byte[] data = sendDataQueue.take();
                        if (data != null && data.length>0){
                            requestDataBuffer.clear();
                            requestDataBuffer.put(data);
                            requestDataBuffer.flip();
                            socketChannel.write(requestDataBuffer);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 关闭该客户端
     */
    public static void shutDown(){
        try {
            if (socketChannel!=null){
                socketChannel.close();
            }
            if (selector!=null){
                selector.close();
            }
        } catch ( Exception e){
            e.printStackTrace();
        }
        isShutDown = true;
        executor.shutdownNow();
    }
	
    
    public static void main(String[] args) throws Exception {
		startService();
		
		RequestBean request = new RequestBean("1", SocketAction.sign);
		request.t = System.currentTimeMillis();
		SignRequest content = new SignRequest();
		content.deviceId = "xxxxx0000";
		content.osVersion = "macOS";
		content.appVersion = "1.0";
		content.space = 1024944464l;
		request.setContent(content);
		
		sendData((GsonUtils.toJson(request) + "###").getBytes());
		
		Thread.sleep(10000);
		
		sendData((GsonUtils.toJson(request) + "###").getBytes());
		
	}
}
