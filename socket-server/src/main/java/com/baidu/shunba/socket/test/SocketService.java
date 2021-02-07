package com.baidu.shunba.socket.test;


import com.baidu.shunba.socket.bean.RequestBean;
import com.baidu.shunba.socket.bean.ResponseBean;
import com.baidu.shunba.socket.bean.SocketAction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketService {
    private static final String TAG = SocketService.class.getSimpleName();


    public static interface ISocketDataHandler {
        public void didWritable();
        public void didGetResponse(ResponseBean response);
        public void didGetError(String error);
    }

    private static final String ServerName = "192.168.50.233";
    private static final int ServerPort = 7890;
    private static final String ResponseSplit = "###";

    private static final long MaxRequestInterval = 30 * 1000l;
    private static final long MaxConnectInterval = 10 * 1000l;
    private static final int DATA_MAX_LEN = 0xFFFF;

    private static enum Status {
        Closed,
        Starting,
        Connecting,
        Connected,
        ;
    }

    private static interface SocketConnectHandler {
        public void handlerAcceptable(SocketChannel client);
        public void handlerConnectable(SocketChannel client);
        public void handlerReadable(SocketChannel client);
    }

    private static volatile ExecutorService executor;
    private static volatile Thread monitor;

    //private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static SocketChannel socketChannel;
    private static Selector selector;

    private static final BlockingQueue<byte[]> normalSendDataQueue = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<byte[]> urgentsendDataQueue = new ArrayBlockingQueue<>(100);

    private static final ByteArrayOutputStream responseDataBuffer = new ByteArrayOutputStream(DATA_MAX_LEN);


    private static final WeakHashMap<ISocketDataHandler, String> handlers = new WeakHashMap<>();

    private static volatile Status curStatus = Status.Closed;

    private static volatile boolean isWritteReady = false;

    private static volatile boolean shutDownSign = false;

    private static volatile long socketBeginConnectTime = 0l;
    private static volatile long lastRequestTime = 0l;

    public static boolean isRunning() {
        return curStatus != Status.Closed;
    }

    public static void addDataHandler(ISocketDataHandler dataHandler) {
        handlers.put(dataHandler, "");
    }

    public static void removeDataHandler(ISocketDataHandler dataHandler) {
        handlers.remove(dataHandler);
    }

    public static void sendRequest(RequestBean request) {
        sendData(request.getAction().isUrgent, (request + ResponseSplit).getBytes(StandardCharsets.UTF_8));
    }

    private static void sendHeartRequest() {
        RequestBean request = new RequestBean(System.currentTimeMillis() + "", SocketAction.heart);
        sendData(request.getAction().isUrgent, (request + ResponseSplit).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 发送任务处理线程
     *
     * @param data byte[]
     */
    private static void sendData(final boolean isUrgent, final byte[] data) {
        if (data == null) {
            return;
        }
        try {
            if (isUrgent) {
                urgentsendDataQueue.offer(data);
            } else {
                normalSendDataQueue.offer(data);
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "push msg", e);
        }
    }

    /**
     * 1 启动连接及读数据监听线程；
     * 2 启动发数据监听线程；
     */
    public static synchronized void startService() {
        startSocketConnect();

        beginMonitor();
    }

    private static final SocketConnectHandler connectHandler = new SocketConnectHandler() {
        @Override
        public void handlerAcceptable(SocketChannel client) {
//            //LogUtils.d(TAG,"与服务端口["+serverIp+":"+port+"]连接成功");
//            SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
//            //非阻塞
//            clientChannel.configureBlocking(false);
//            //注册selector
//            clientChannel.register(key.selector(), SelectionKey.OP_READ);
//            System.out.println("建立请求......");
        }

        public void handlerConnectable(SocketChannel client) {
            //LogUtils.d(TAG,"与服务端口["+serverIp+":"+port+"]连接成功");
            try {
                if (client.finishConnect()) {
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    //countDownLatch.countDown();
                    beginWritter();
                }
            } catch (Exception e) {
                LogUtils.e(TAG, "open read", e);
                shutDown();
                handlerError("不能连接服务器：" + e);
            }
        }

        public void handlerReadable(SocketChannel client) {
            try {
                //数据缓冲
                ByteBuffer dataBuffer = ByteBuffer.allocate(DATA_MAX_LEN);
                dataBuffer.clear();

                int readLen = 0;
                try {
                    readLen = client.read(dataBuffer);
                } catch (IOException e) {
                    //TODO, 处理连接被关闭的情况，如果是ID被别人顶替了，界面需要提示。
                    LogUtils.e(TAG, "read err", e);
                    shutDown();
                    handlerError("不能从服务器读取数据：" + e);
                    return;
                }

                if (readLen <= 0) {
                    LogUtils.e(TAG, "read 0");
                    shutDown();
                    handlerError("不能从服务器读取数据");
                    return;
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
                        handlerResponseString(resp);
                    }
                }

                //LogUtils.d(TAG, PUtils.toHexStr(result));
            } catch (Exception e) {
                LogUtils.e(TAG, "open read", e);
                shutDown();
            }
        }


    };




    private static synchronized void startSocketConnect() {
        try {
            if (socketChannel != null) {
                return;
            }

            if (executor != null && !executor.isShutdown()) {
                executor.shutdownNow();
            }
            executor = Executors.newFixedThreadPool(2);

            socketBeginConnectTime = System.currentTimeMillis();

            //启动及接收数据线程
            executor.execute(() -> {
                try {
                    curStatus = Status.Starting;

                    socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(false);
                    selector = Selector.open();
                    SocketAddress socketAddress = new InetSocketAddress(ServerName, ServerPort);
                    //连接服务端socket
                    socketChannel.connect(socketAddress);
                    socketChannel.register(selector, SelectionKey.OP_CONNECT);

                    //selector处理
                    while (!shutDownSign) {
                        selector.select();
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = keys.iterator();
                        SocketChannel client;
                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            keyIterator.remove();

                            client = (SocketChannel) key.channel();
                            if (key.isAcceptable()) {
                                connectHandler.handlerAcceptable(client);
                            } else if (key.isConnectable()) {
                                curStatus = Status.Connecting;
                                connectHandler.handlerConnectable(client);
                            } else if (key.isReadable()) {
                                curStatus = Status.Connected;
                                connectHandler.handlerReadable(client);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "open read", e);
                    shutDown();
                    handlerError("不能从服务器读取数据：" + e);
                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, "connect to server", e);
            shutDown();
            handlerError("连接服务器错误：" + e);
        }
    }

    private static synchronized void beginWritter() {
        if (socketChannel != null) {
            return;
        }

        executor.execute(() -> {
            try {
                isWritteReady = true;

                //countDownLatch.await();

                lastRequestTime = System.currentTimeMillis();
                handlerWritable();

                while (!shutDownSign) {
                    if (socketChannel != null && socketChannel.isConnected()) {
                        byte[] data;
                        if (!urgentsendDataQueue.isEmpty()) {
                            data = urgentsendDataQueue.take();
                        } else if (!normalSendDataQueue.isEmpty()) {
                            data = normalSendDataQueue.take();
                        } else {
                            tryToSleep(100l);
                            continue;
                        }

                        if (data == null || data.length <= 0) {
                            tryToSleep(100l);
                            continue;
                        }

                        lastRequestTime = System.currentTimeMillis();

                        ByteBuffer requestDataBuffer = ByteBuffer.allocate(DATA_MAX_LEN);
                        requestDataBuffer.put(data);
                        requestDataBuffer.flip();
                        socketChannel.write(requestDataBuffer);
                    } else {
                        tryToSleep(100l);
                    }
                }
            } catch (Exception e) {
                LogUtils.e(TAG, "write to server", e);
                shutDown();
                handlerError("不能写入数据到服务器数据：" + e);
            }
        });
    }

    private static synchronized void beginMonitor() {
        if (monitor != null) {
            return;
        }

        monitor = new Thread() {
            @Override
            public void run() {
                while (!shutDownSign) {
                    try {
                        if (curStatus == Status.Connected) {
                            if (System.currentTimeMillis() - lastRequestTime > MaxRequestInterval) {
                                sendHeartRequest();
                            }
                        } else if (curStatus != Status.Connected) {
                            if (System.currentTimeMillis() - socketBeginConnectTime > MaxConnectInterval) {
                                shutDown();
                                tryToSleep(1000l);
                                startSocketConnect();
                            }
                        }
                    } catch (Exception e) {
                    }

                    tryToSleep(1000l);
                }
            }
        };

        monitor.start();
    }

    private static void tryToSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            LogUtils.w(TAG, "sleep", e);
        }
    }

    /**
     * 关闭该客户端
     */
    public static synchronized void stopService() {
        shutDownSign = true;

        shutDown();

        try {
            if (monitor != null) {
                monitor.interrupt();
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "Thread.interrupt", e);
        }
        monitor = null;
    }

    private static void shutDown() {
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "socketChannel.close", e);
        }

        try {
            if (selector != null) {
                selector.close();
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "selector.close", e);
        }

        curStatus = Status.Closed;

        urgentsendDataQueue.clear();
        responseDataBuffer.reset();

        isWritteReady = false;

        socketChannel = null;
        selector = null;

        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
        executor = null;
    }











    private static void handlerResponseString(String responseString) {
        final ResponseBean response = ResponseBean.fromString(responseString);
        System.err.println(response);
    }

    private static void handlerResponseInMainThread(final ResponseBean response) {
        try {
            for (ISocketDataHandler handler : handlers.keySet()) {
                try {
                    handler.didGetResponse(response);
                } catch (Exception e) {
                    LogUtils.w(TAG, "handlerResponseInMainThread", e);
                }
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "handlerResponseInMainThread", e);
        }
    }


    private static void handlerError(final String err) {
    	System.err.println(err);
    }

    private static void handlerErrorInMainThread(final String err) {
        try {
            for (ISocketDataHandler handler : handlers.keySet()) {
                try {
                    handler.didGetError(err);
                } catch (Exception e) {
                    LogUtils.w(TAG, "handlerErrorInMainThread", e);
                }
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "handlerErrorInMainThread", e);
        }
    }

    private static void handlerWritable() {
    	System.err.println("handlerWritable");
    }

    public static void main(String[] args) {
		startService();
		
	}

}
