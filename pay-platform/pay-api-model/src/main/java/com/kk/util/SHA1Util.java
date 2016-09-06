package com.kk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha1 加密
 * 
 * @author zhihui.kong
 * 
 */
public class SHA1Util {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String sha1(String content) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.update(content.getBytes());
		byte[] bytes = messageDigest.digest();
		String sha1 = encodeHex(bytes);
		return sha1;
	}

	private static String encodeHex(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}

		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(sha1("kongzhidea"));

	}

}
