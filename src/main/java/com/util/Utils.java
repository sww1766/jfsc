package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	/**
	 * 字符串转换成为16进制
	 * @param str
	 * @return
	 */
	public static String str2HexStr(String str) {
	    char[] chars = "0123456789abcdef".toCharArray();
	    StringBuilder sb = new StringBuilder("");
	    byte[] bs = str.getBytes();
	    int bit;
	    for (int i = 0; i < bs.length; i++) {
	        bit = (bs[i] & 0x0f0) >> 4;
	        sb.append(chars[bit]);
	        bit = bs[i] & 0x0f;
	        sb.append(chars[bit]);
	    }
	    return sb.toString().trim();
	}

	/**
	 * 16进制直接转换成为字符串
	 * @param hexStr
	 * @return
	 */
	public static String hexStr2Str(String hexStr) {
	    String str = "0123456789abcdef";
	    char[] hexs = hexStr.toCharArray();
	    byte[] bytes = new byte[hexStr.length() / 2];
	    int n;
	    for (int i = 0; i < bytes.length; i++) {
	        n = str.indexOf(hexs[2 * i]) * 16;
	        n += str.indexOf(hexs[2 * i + 1]);
	        bytes[i] = (byte) (n & 0xff);
	    }
	    return new String(bytes);
	}
	
	/**
	 * 时间戳转时间
	 * @param s
	 * @return
	 */
	public static String stampToDate(String seconds){
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }
	
//	public static void main(String[] args) {
//		String str = "{\"f3r饭到\":\"方式\"}";
//		System.out.println(str2HexStr(str));
//		System.out.println(hexStr2Str("0x7b22663372e9a5ade588b0223a22e696b9e5bc8f227d".replaceAll("^0[x|X]", "")));
//	}
}
