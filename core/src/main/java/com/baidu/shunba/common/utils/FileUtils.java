package com.baidu.shunba.common.utils;

public class FileUtils {

	public static final String fileExtendSplit = ".";
	public static final char pathSplit = '/';
	
	public static String getFileType(Object filePath) {
		try {
			String path = filePath.toString();
			String typeSplit = ".";
			String type = path.substring(path.lastIndexOf(typeSplit) + typeSplit.length());
			return type.toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileNameWithExtend(final String url) {
		String fileName = url.substring(url.lastIndexOf(pathSplit) + 1);
		return fileName;
	}
	
	public static String getFileNameWithoutExtend(final String url) {
		final String fullFile = getFileNameWithExtend(url);
		try {
			return fullFile.substring(0, fullFile.lastIndexOf(fileExtendSplit));
		} catch(Exception ex) {
			return fullFile;
		}
	}
	
	//获取后缀名
	public static String getExtend(String filepath) {
		if (filepath != null && filepath.lastIndexOf('.') > 0) {
			return filepath.substring(filepath.lastIndexOf('.') + 1,filepath.length());
		}
		return "";
	}
	
}
