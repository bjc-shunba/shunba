package com.baidu.shunba.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 * 
 * https://www.cnblogs.com/catalina-/p/6001831.html
 * https://www.cnblogs.com/liunanjava/p/4297854.html 
 * https://www.cnblogs.com/tancky/p/6409823.html
 * https://www.cnblogs.com/Darlin356230410/p/8602674.html
 */
public class CryptUtils {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CryptUtils.class);
	
	private static final String defaultAESSeed = "seed_chris_9999999";
	private static final byte[] defaultAESKey = new byte[] { 59,-25,90,98,61,17,27,3,60,118,48,-109,-36,51,74,-99 }; // 通过 printSecretKeyBytes 生成
	
	public static SecretKey getSecretKey(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed.getBytes("UTF-8"));
		keyGenerator.init(128, random);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}
	
	public static SecretKey getDefaultSecretKey() {
		return new SecretKeySpec(defaultAESKey, "AES");
	}
	
	/**
	 * AES 加密，采用默认密钥
	 * @param src
	 * @return
	 */
	public static String AESEncrypt(String src) {
		return AESEncrypt(src, getDefaultSecretKey());
	}
	
	public static String AESEncrypt(String src, String seed) {
		try {
			return AESEncrypt(src, getSecretKey(seed));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error(src, e);
		}
		throw new UnsupportedOperationException(src);
	}
	
	public static String AESEncrypt(String src, SecretKey secretKey) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encodeResult = cipher.doFinal(src.getBytes("UTF-8"));
			return new String(Base64.getUrlEncoder().encode(encodeResult), "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			logger.error(src, e);
		} catch (NoSuchPaddingException e) {
			logger.error(src, e);
		} catch (InvalidKeyException e) {
			logger.error(src, e);
		} catch (IllegalBlockSizeException e) {
			logger.error(src, e);
		} catch (BadPaddingException e) {
			logger.error(src, e);
		} catch (UnsupportedEncodingException e) {
			logger.error(src, e);
		}
		
		throw new UnsupportedOperationException(src);
	}
	
	
	public static String AESDecrypt(String encrypted) {
		return AESDecrypt(encrypted, getDefaultSecretKey());
	}
	
	public static String AESDecrypt(String encrypted, String seed) {
		try {
			return AESDecrypt(encrypted, getSecretKey(seed));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error(encrypted, e);
		}
		throw new UnsupportedOperationException(encrypted);
	}
	
	public static String AESDecrypt(String encrypted, SecretKey secretKey) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodeResult = cipher.doFinal(Base64.getUrlDecoder().decode(encrypted.getBytes("UTF-8")));
			return new String(decodeResult, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			logger.error(encrypted, e);
		} catch (NoSuchPaddingException e) {
			logger.error(encrypted, e);
		} catch (InvalidKeyException e) {
			logger.error(encrypted, e);
		} catch (IllegalBlockSizeException e) {
			logger.error(encrypted, e);
		} catch (BadPaddingException e) {
			logger.error(encrypted, e);
		} catch (UnsupportedEncodingException e) {
			logger.error(encrypted, e);
		}
		
		throw new UnsupportedOperationException(encrypted);
	}
	
	public static void printSecretKeyBytes(String seed) {
		try {
			SecretKey sk = getSecretKey(seed);
			byte[] bytes = sk.getEncoded();
			
			System.out.println(bytes.length);
			
			System.out.print("{");
			for (int i = 0; i < bytes.length; i++) {
				if (i != 0) {
					System.out.print(",");
				}
				System.out.print(bytes[i]);
			}
			System.out.println("}");
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void mainTest(String[] args) {
		printSecretKeyBytes(defaultAESSeed);
		
		String encrypted = AESEncrypt("36");
		String decrypted = AESDecrypt(encrypted);
		System.out.println(encrypted);
		System.out.println(decrypted);
		
		encrypted = AESEncrypt("36", defaultAESSeed);
		decrypted = AESDecrypt(encrypted, defaultAESSeed);
		System.out.println(encrypted);
		System.out.println(decrypted);
		
		System.out.println(AESDecrypt("CKdfpqGWfRse0FPT21gtxA=="));
	}

}
