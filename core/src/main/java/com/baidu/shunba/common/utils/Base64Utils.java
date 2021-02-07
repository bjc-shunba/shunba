package com.baidu.shunba.common.utils;

import java.io.*;
import java.util.Base64;

public class Base64Utils {

	public static String atob(String str) {
		try {
			return new String(Base64.getDecoder().decode(str), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String decode(String str) {
		return atob(str);
	}


	public static String btoa(String str) {
		try {
			return new String(Base64.getEncoder().encode(str.getBytes("UTF-8")), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String encode(String str) {
		return btoa(str);
	}


	public static String encode(byte[] bytes) {
		try {
			return new String(Base64.getEncoder().encode(bytes), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * base64字符串转化成文件，可以是JPEG、PNG、TXT和AVI等等
	 *
	 * @param base64
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static boolean decodeToFile(String base64, String filePath) {
		return decodeToFile(base64, new File(filePath));
	}

	public static boolean decodeToFile(String base64, File file) {
		// 数据为空
		if (base64 == null) {
			return false;
		}

		// Base64解码,对字节数组字符串进行Base64解码并生成文件
		byte[] byt = Base64.getDecoder().decode(base64);
		for (int i = 0, len = byt.length; i < len; ++i) {
			// 调整异常数据
			if (byt[i] < 0) {
				byt[i] += 256;
			}
		}
		OutputStream out = null;
		InputStream input = new ByteArrayInputStream(byt);
		try {
			// 生成指定格式的文件
			out = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = input.read(buff)) != -1) {
				out.write(buff, 0, len);
			}

			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
				}
			} catch (Exception e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return true;
	}

	public static String encodeFromFile(String filePath) {
		return encodeFromFile(new File(filePath));
	}

	public static String encodeFromFile(File file) {
		InputStream in = null;
		byte[] data = null;
		// 读取文件字节数组
		try {
			in = new FileInputStream(file);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return encode(data);
	}

}
