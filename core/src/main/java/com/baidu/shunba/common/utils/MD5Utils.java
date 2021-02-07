package com.baidu.shunba.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class MD5Utils {

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// MD5 authKey
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.err.println(e.toString());
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	public static void mainTest(String[] args) {
		System.out.println(MD5Utils.MD5("aaa"));
		System.out.println(MD5Utils.string2MD5("aaa"));
		
		System.out.println(MD5Utils.MD5("aaa").endsWith(MD5Utils.string2MD5("aaa")));

		int iTen = Integer.parseInt("e", 16);
		System.out.println(iTen);
		
		
		System.out.println(genInitCode().length());
		System.out.println(genInitCode().length());
		System.out.println(genInitCode().length());
		System.out.println(genInitCode().length());
	}
	
	private static String genInitCode() {
		String md5Str = getMD5Str();
		return md5Str.substring(md5Str.length() - 10);
	}

	private static String getMD5Str() {
		String md5Str = MD5Utils.MD5(String.valueOf(System.currentTimeMillis()));
		return md5Str;
	}
	
	public static String getFileMD5(File file) throws Exception {
		String value = "";
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (Exception ex) {
				}
			}
		}
		return value;
	}
}
