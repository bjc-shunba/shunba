package com.baidu.shunba.socket.bean;

public enum SocketAction {

	sign("注册", true, true, false),
	init("初始化", true, true, false),
	pushInvalid("设备注销", true, true, false),
	pushAuth("授权信息推送", true, true, false),
	sendDispatch("收发车推送", true, true, false),
	authBack("授权码回调", true, true, false),
	pushConfig("配置推送", true, true, false),
	heart("心跳", false, true, false),
	newVersion("新版本", true, true, false),
	newFace("新人脸", true, true, false),
	newTicket("新票据", true, true, false),
	newTickets("新批量票据", true, true, false),
	uploadTickets("上传核销数据", true, true, false),
	
	log("日志消息", false, true, false),
	
	unknow("未知", false, false, true),
	;

	public final String memo;
	public final boolean isUrgent;
	public final boolean needProcess;
	public final boolean needClose;
	
	private SocketAction(String memo, boolean isUrgent, boolean needProcess, boolean needClose) {
		this.memo = memo;
		this.isUrgent = isUrgent;
		this.needProcess = needProcess;
		this.needClose = needClose;
	}
	
	public static SocketAction getAction(String name) {
		for (SocketAction a : values()) {
			if (a.name().equals(name)) {
				return a;
			}
		}
		return unknow;
	}
	
	
}
