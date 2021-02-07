package com.baidu.shunba.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class HttpUtils {
	public static enum ContentType {
		HTML, FORM, XFORM, JSON,
	}

	public static String generateUrl(String baseUrl, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(baseUrl);
		boolean firstCheck = true;
		for (Map.Entry<String, Object> param : params.entrySet()) {
			try {
				String val = param.getValue() == null ? "" : param.getValue().toString();
				if (firstCheck) {
					if (url.indexOf("?") >= 0) {
						url.append("&" + param.getKey() + "=" + URLEncoder.encode(val, "utf-8"));
					} else {
						url.append("?" + param.getKey() + "=" + URLEncoder.encode(val, "utf-8"));
					}
					firstCheck = false;
				} else {
					url.append("&" + param.getKey() + "=" + URLEncoder.encode(val, "utf-8"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return url.toString();
	}

	public static String generatePostBody(Map<String, Object> params) {
		StringBuilder postBody = new StringBuilder();
		boolean firstCheck = true;
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (firstCheck) {
				postBody.append(param.getKey() + "=" + param.getValue());
				firstCheck = false;
			} else {
				postBody.append("&" + param.getKey() + "=" + param.getValue());
			}
		}
		return postBody.toString();
	}

	//
	public static String fetchHttpUrlResponse(String url, ContentType type, String postBody) {
		return fetchHttpUrlResponse(null, url, type, postBody, 15 * 1000, 30 * 1000);
	}

	public static String fetchHttpUrlResponse(String url, ContentType type, String postBody, int connectTimeout, int readTimeout) {
		return fetchHttpUrlResponse(null, url, type, postBody, connectTimeout, readTimeout);
	}

	// for auth
	public static String fetchHttpUrlResponse(ProcessHttpHandler handler, String url, ContentType type, String postBody) {
		return fetchHttpUrlResponse(handler, url, type, postBody, 15 * 1000, 30 * 1000);
	}

	public static String fetchHttpUrlResponse(ProcessHttpHandler handler, String url, ContentType type, String postBody, int connectTimeout, int readTimeout) {
		StringBuilder responseRes = new StringBuilder("");
		try {
			boolean hasPostBody = postBody != null && postBody.length() > 0;

			URL unifiedOrderUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) unifiedOrderUrl.openConnection();
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			if (type == null) {
				connection.setRequestProperty("Content-Type", "text/xml");
			} else {
				switch (type) {
				case FORM:
					connection.setRequestProperty("Content-Type", "application/form-data");
					break;
				case XFORM:
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					break;
				case JSON:
					connection.setRequestProperty("Content-Type", "application/json");
					break;
				default:
					connection.setRequestProperty("Content-Type", "text/xml");
					break;
				}
			}

			if (hasPostBody) {
				connection.setDoOutput(true);
			}

			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "utf-8");

			connection.connect();

			OutputStream out = null;
			if (hasPostBody) {
				out = connection.getOutputStream();
				out.write(postBody.getBytes("utf-8"));
				out.flush();
			}
			
			try {
				if (handler != null) {
					int statusCode = connection.getResponseCode();
					handler.didGetStatusCode(statusCode);
				}
			} catch (Exception e) {
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				responseRes.append(line);

				try {
					if (handler != null) {
						handler.didGetLine(line);
					}
				} catch (Exception e) {
				}
			}

			if (hasPostBody) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}

			br.close();
		} catch (Exception exp) {
			try {
				if (handler != null) {
					handler.didGetError(exp.getMessage());
				}
			} catch (Exception e) {
			}
			return exp.getMessage();
		}
		return responseRes.toString();
	}

	public static int fetchHttpUrlStatusCode(String url, ContentType type, String postBody) {
		return fetchHttpUrlStatusCode(url, type, postBody, 15 * 1000, 30 * 1000);
	}

	public static int fetchHttpUrlStatusCode(String url, ContentType type, String postBody, int connectTimeout, int readTimeout) {
		try {
			boolean hasPostBody = postBody != null && postBody.length() > 0;

			URL unifiedOrderUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) unifiedOrderUrl.openConnection();
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			if (type == null) {
				connection.setRequestProperty("Content-Type", "text/xml");
			} else {
				switch (type) {
				case FORM:
					connection.setRequestProperty("Content-Type", "application/form-data");
					break;
				case XFORM:
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					break;
				default:
					connection.setRequestProperty("Content-Type", "text/xml");
					break;
				}
			}

			if (hasPostBody) {
				connection.setDoOutput(true);
			}

			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "utf-8");

			connection.connect();

			OutputStream out = null;
			if (hasPostBody) {
				out = connection.getOutputStream();
				out.write(postBody.getBytes("utf-8"));
				out.flush();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			StringBuilder responseRes = new StringBuilder();
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				responseRes.append(line);
			}

			if (hasPostBody) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}

			br.close();

			return connection.getResponseCode();
		} catch (Exception exp) {
			return 999;
		}
	}

	public static long featchUrlSize(String url) {
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			return uc.getContentLengthLong();
		} catch (Exception e) {
		}
		return 0l;
	}

	public static String getIp(HttpServletRequest request) {
		String ip = "127.0.0.1";

		try {
			ip = request.getHeader("X-Real-IP");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Forwarded-For");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} catch (Exception e) {
		}

		return ip;
	}

	public static Map<String, String> getUrlParamMap(String url) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			String[] kvs = url.trim().split("&");
			for (String kvp : kvs) {
				try {
					String[] kv = kvp.split("=");

					if (kv.length < 2 || kv[1] == null || kv[1].length() == 0) {
						continue;
					}

					String key = URLDecoder.decode(kv[0], "utf-8");
					params.put(key, null);
					params.put(key, URLDecoder.decode(kv[1], "utf-8"));
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
		return params;
	}

	public static String getRequestPostString(HttpServletRequest request) throws Exception {
		byte buffer[] = getRequestPostBytes(request);
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		return new String(buffer, charEncoding);
	}

	public static byte[] getRequestPostBytes(HttpServletRequest request) throws Exception {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = request.getInputStream().read(buffer, i, contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

	public synchronized static String getServerUrl(HttpServletRequest request) {
		String serverUrl = null;
		if (serverUrl == null && request != null) {
			String domain = request.getHeader("Host");
			if (domain == null || domain.length() == 0) {
				domain = request.getServerName() + ":" + request.getServerPort();
			}
			serverUrl = request.getScheme() + "://" + domain;
		}
		return serverUrl;
	}

	public synchronized static String getProjectUrl(HttpServletRequest request) {
		String projectUrl = null;
		if (projectUrl == null && request != null) {
			projectUrl = getServerUrl(request) + request.getContextPath();
		}
		return projectUrl;
	}

	public static final String getRequestUrl(HttpServletRequest request) {
		String query = request.getQueryString();
		return getServerUrl(request) + request.getRequestURI() + (query == null || query.length() == 0 ? "" : "?" + query);
	}

	public static interface ProcessHttpHandler {
		public void didGetStatusCode(int statusCode);

		public void didGetError(String error);

		public void didGetLine(String line);
	}

	public static void processHttpUrlResponse(ProcessHttpHandler handler, String url, ContentType type, String postBody, int connectTimeout, int readTimeout) {
		try {
			boolean hasPostBody = postBody != null && postBody.length() > 0;

			URL unifiedOrderUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) unifiedOrderUrl.openConnection();
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			if (type == null) {
				connection.setRequestProperty("Content-Type", "text/xml");
			} else {
				switch (type) {
				case FORM:
					connection.setRequestProperty("Content-Type", "application/form-data");
					break;
				case XFORM:
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					break;
				case JSON:
					connection.setRequestProperty("Content-Type", "application/json");
					break;
				default:
					connection.setRequestProperty("Content-Type", "text/xml");
					break;
				}
			}

			if (hasPostBody) {
				connection.setDoOutput(true);
			}

			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "utf-8");

			connection.connect();

			OutputStream out = null;
			if (hasPostBody) {
				out = connection.getOutputStream();
				out.write(postBody.getBytes("utf-8"));
				out.flush();
			}
			
			try {
				if (handler != null) {
					int statusCode = connection.getResponseCode();
					handler.didGetStatusCode(statusCode);
				}
			} catch (Exception e) {
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				try {
					handler.didGetLine(line);
				} catch (Exception e) {
				}
			}

			if (hasPostBody) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}

			br.close();
		} catch (Exception exp) {
			try {
				if (handler != null) {
					handler.didGetError(exp.getMessage());
				}
			} catch (Exception e) {
			}
		}
	}

	public static long downloadHttpUrlResponse(String storePath, String url, ContentType type, String postBody, int connectTimeout, int readTimeout) {
		return downloadHttpUrlResponse(null, storePath, url, type, postBody, connectTimeout, readTimeout);
	}

	public static long downloadHttpUrlResponse(ProcessHttpHandler handler, String storePath, String url, ContentType type, String postBody, int connectTimeout, int readTimeout) {
		long count = 0;
		BufferedWriter write = null;
		try {
			File storeOut = new File(storePath);
			if (!storeOut.exists()) {
				File outFolder = storeOut.getParentFile();
				outFolder.mkdirs();
			}
			write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(storePath))));

			boolean hasPostBody = postBody != null && postBody.length() > 0;

			URL unifiedOrderUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) unifiedOrderUrl.openConnection();
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			if (type == null) {
				connection.setRequestProperty("Content-Type", "text/xml");
			} else {
				switch (type) {
				case FORM:
					connection.setRequestProperty("Content-Type", "application/form-data");
					break;
				case XFORM:
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					break;
				case JSON:
					connection.setRequestProperty("Content-Type", "application/json");
					break;
				default:
					connection.setRequestProperty("Content-Type", "text/xml");
					break;
				}
			}

			if (hasPostBody) {
				connection.setDoOutput(true);
			}

			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "utf-8");

			connection.connect();

			OutputStream out = null;
			if (hasPostBody) {
				out = connection.getOutputStream();
				out.write(postBody.getBytes("utf-8"));
				out.flush();
			}

			try {
				if (handler != null) {
					int statusCode = connection.getResponseCode();
					handler.didGetStatusCode(statusCode);
				}
			} catch (Exception e) {
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				try {
					if (count > 0) {
						write.newLine();
					}
					write.write(line);
				} catch (Exception e) {
				}

				try {
					if (handler != null) {
						handler.didGetLine(line);
					}
				} catch (Exception e) {
				}

				count += 1l;
			}

			if (hasPostBody) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}

			br.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			try {
				if (handler != null) {
					handler.didGetError(exp.getMessage());
				}
			} catch (Exception e) {
			}
		} finally {
			try {
				if (write != null) {
					write.flush();
					write.close();
				}
			} catch (Exception e) {
			}
		}
		return count;
	}

	public static void mainTest(String[] args) {
		String url = "http://www.baidu.com/";
		
		fetchHttpUrlResponse(new ProcessHttpHandler() {
			@Override
			public void didGetStatusCode(int statusCode) {
				System.err.println("get code:" + statusCode);
			}
			
			@Override
			public void didGetLine(String line) {
				System.out.println("get line:" + line);
			}
			
			@Override
			public void didGetError(String error) {
				System.err.println("get error:" + error);
			}
		}, url, ContentType.HTML, null);
		
		// System.err.println(fetchHttpUrlStatusCode("https://www.baidu.com",
		// ContentType.HTML, null));

		String path = "/data/uploads/menuimg/files/wechat/bill/20190227";
		File out = new File(path);
		if (!out.exists()) {
			File outFolder = out.getParentFile();
			System.out.println(outFolder);
		}
	}
}
