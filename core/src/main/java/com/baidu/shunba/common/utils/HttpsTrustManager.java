package com.baidu.shunba.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class HttpsTrustManager implements X509TrustManager {

	/*
	 * The default X509TrustManager returned by SunX509. We’ll delegate
	 * decisions to it, and fall back to the logic in this class if the default
	 * X509TrustManager doesn’t trust it.
	 */
	X509TrustManager sunJSSEX509TrustManager;

	public HttpsTrustManager(String password) throws Exception {
		InputStream cerIS = HttpsTrustManager.class.getClassLoader().getResourceAsStream("apiclient_cert.p12");
		System.out.println("xxxxxxx(" + cerIS + "):" + cerIS.available());
		// create a "default" JSSE X509TrustManager.

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(HttpsTrustManager.class.getClassLoader().getResourceAsStream("apiclient_cert.p12"), password.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); //TrustManagerFactory.getInstance("SunX509", "SunJSSE");

		tmf.init(ks);

		TrustManager tms[] = tmf.getTrustManagers();

		/*
		 * Iterate over the returned trustmanagers, look for an instance of
		 * X509TrustManager. If found, use that as our "default" trust manager.
		 */
		for (int i = 0; i < tms.length; i++) {
			if (tms[i] instanceof X509TrustManager) {
				sunJSSEX509TrustManager = (X509TrustManager) tms[i];
				return;
			}
		}

		/*
		 * Find some other way to initialize, or else we have to fail the
		 * constructor.
		 */
		throw new Exception("Couldn’t initialize");
	}

	/*
	 * Delegate to the default trust manager.
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		try {
			sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Delegate to the default trust manager.
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		try {
			sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Merely pass this through.
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return sunJSSEX509TrustManager.getAcceptedIssuers();
	}
	
	public static String getHttpsUrlResponse(String url, String password, String postBody) {
		StringBuilder responseRes = new StringBuilder("");
		try {
			boolean hasPostBody = postBody != null && postBody.length() > 0;
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new HttpsTrustManager(password) };
			SSLContext sslContext = SSLContext.getInstance("TLSv1");    //SSLContext.getInstance("SSL", "SunJSSE");

			sslContext.init(null, tm, new SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			// 创建URL对象
			URL myURL = new URL(url);

			// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
			httpsConn.setSSLSocketFactory(ssf);
			httpsConn.setUseCaches(false);
			httpsConn.setDoInput(true);
			
			if (hasPostBody) {
				httpsConn.setDoOutput(true);
			}

			httpsConn.connect();

			OutputStream out = null;
			if (hasPostBody) {
				out = httpsConn.getOutputStream();
				out.write(postBody.getBytes("utf-8"));
				out.flush();
			}
			
			// 取得该连接的输入流，以读取响应内容
			BufferedReader br = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), "UTF-8"));
	        
	        for (String line = br.readLine(); line != null; line = br.readLine()) {
	        	responseRes.append(line); 
	        }
	        
	        if (hasPostBody) {
	        	try {
	        		out.close(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        
	        br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseRes.toString();
	}
	
	public static void mainTest(String[] args) {
		File file = new File("/Users/Chris/Documents/workspace_mars/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/erp/WEB-INF/classes/apiclient_cert.p12");
		System.out.println(file.exists());
		System.out.println(TrustManagerFactory.getDefaultAlgorithm());
	}
}
