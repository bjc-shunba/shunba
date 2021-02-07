package com.baidu.shunba.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;


public class SUSerialize {
	private static final Logger log = Logger.getLogger(SUSerialize.class);
	
	private final String version;
	private final String DatePath;
	private final String DateFile;
	
	
	public static SUSerialize newInstance(String version, String datePath, String dateFile) {
		return new SUSerialize(version, datePath, dateFile);
	}
	
	public SUSerialize(String version, String datePath, String dateFile) {
		super();
		this.version = version;
		DatePath = datePath;
		DateFile = dateFile;
	}

	private synchronized File getStoreFile() {
		File file = new File(DatePath + "/" + DateFile + version);
		if (!file.exists()) {
			//build path
			File path = new File(DatePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			
			//create file
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("create file error", e);
			}
		}
		return file;
	}
	
	public synchronized void storeData(Serializable data) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getStoreFile()));
			oos.writeObject(data);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("storeDate", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends Serializable> T restoreData() {
		T data = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getStoreFile()));
			data = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			log.warn("restoreDate", e);
		}
		return data;
	}
	
	public synchronized <T extends Serializable> T restoreData(T defaultValue) {
		T data = defaultValue;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getStoreFile()));
			data = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			log.warn("restoreDate", e);
		}
		return data;
	}
}
