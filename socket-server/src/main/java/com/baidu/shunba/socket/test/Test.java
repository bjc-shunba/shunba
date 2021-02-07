package com.baidu.shunba.socket.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.shunba.common.gson.GsonUtils;
import com.baidu.shunba.socket.bean.RequestBean;
import com.baidu.shunba.socket.bean.SocketAction;
import com.baidu.shunba.socket.bean.req.SignRequest;
import com.baidu.shunba.socket.service.ShunbaSocketService;

public class Test {
	
	private static final List<RequestBean> requests = new ArrayList<>();
	
	public static void main(String[] args) {
		
		try {
			RequestBean request = new RequestBean("1", SocketAction.sign);
			request.t = System.currentTimeMillis();
			SignRequest content = new SignRequest();
			content.deviceId = "xxxxx0000";
			content.osVersion = "macOS";
			content.appVersion = "1.0";
			content.space = 1024944464l;
			request.setContent(content);
			
			requests.add(request);
			requests.add(request);
			
			Socket client = new Socket("127.0.0.1", ShunbaSocketService.portNumber);
			new Thread() {
				final Writer writer = new OutputStreamWriter(client.getOutputStream());  
				public void run() {
					while (true) {
						try {
							for (RequestBean req : requests) {
								writer.write(GsonUtils.toJson(req) + "###");
								writer.flush();// 写完后要记得flush
								
								Thread.sleep(10000);
							}
							
							Thread.sleep(10000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				
				@Override
				protected void finalize() throws Throwable {
					try {
						writer.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					super.finalize();
				}
			}.start();
			
			
			Reader reader = new InputStreamReader(client.getInputStream()); 
			char chars[] = new char[64];  
			int len;
			StringBuilder sb = new StringBuilder();
			while ((len = reader.read(chars)) != -1) {
				String s = new String(chars, 0, len);
				sb.append(s);
				if(sb.indexOf("###") > 0) {
					System.out.println(sb);
					sb.replace(0, sb.indexOf("###") + 3, "");
				}
			}
			reader.close();
			
			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
