package com.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@SuppressWarnings("restriction")
public class CryptoUtil {
	static Logger log = LoggerFactory.getLogger(CryptoUtil.class);

	/**
	 * 3DES 解密
	 *
	 * @param content
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String decryptTripleDesToString(String content, String key) {
		try {
			content = URLDecoder.decode(content, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("3DES解密失败，原因："+e1.getMessage());
			e1.printStackTrace();
		}
		content = content.replaceAll(" ", "+");
		String result = null;

		try {
			// --通过base64,将字符串转成byte数组
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytesrc = decoder.decodeBuffer(content);
			// --解密的key
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);

			// --Chipher对象解密
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			byte[] bytes = cipher.doFinal(bytesrc);

			if (bytes == null) {
				result = "";
			} else {
				result = new String(bytes, "UTF-8");
			}
		} catch (Exception e) {
			log.error("解密失败：" + e);
		}
		
		return result;
	}

	/**
	 * 加密
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encryptTripleDesToString(String content, String key) {
		String result = null;

		try {
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);

			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			byte[] bytes = cipher.doFinal(content.getBytes("UTF-8"));

			BASE64Encoder encoder = new BASE64Encoder();
			result = encoder.encode(bytes).replaceAll("\r", "").replaceAll("\n", "");

		} catch (Exception e) {
			log.error("加密失败：" + e);
		}

		return result;
	}
	
	public static int random6(){
		return (int)((Math.random()*9+1)*100000);
	}

//	public static void main(String[] args) throws UnsupportedEncodingException {
//		JSONObject restlt = new JSONObject();
//		JSONObject oo = new JSONObject();
//		oo.put("a", "22");
//		oo.put("b", "0.3");
//		oo.put("c", "3d");
//		restlt.put("result", oo);
//		
//		String encstr = URLEncoder.encode(encryptTripleDesToString(restlt.toString(),"1cf4a25e2b56a69d0h123456"),"utf-8");
//		System.out.println(encstr);
		
//		String enctest = "1g1SiUr3ewK8p95p3j7LbuNbJJansHt6ZTbaIX355M2QOJCam/ts2MbyITfyE/Bo8Ny7+2J1I2oPUmA57DI7RONW9OpqatbVT43HXWCZUgHjVlfworDf0QABUweft4oVFk6UgSNG5T2cSIdk5mbP/Q==";
//		String decrystr = decryptTripleDesToString(enctest,"1cf4a25e2b56a69d0h282906");
//		System.out.println(decrystr);
//	}
}
