package com.nexon.common.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by MJ on 2018. 8. 23..
 */

public class ComEncDecUtil {

    private static SecretKeySpec getKeySpec(String key) throws Exception {
        byte[] keyBytes = key.getBytes("UTF-8");

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		return keySpec;
    }

    /**
	 * AES128 Encoding
	 * @param text
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getEncrypted(String text, String key) throws Exception {
		if(StringUtils.isEmpty(text) || StringUtils.isEmpty(key)){
			return "";
		}

		byte[] keyBytes = Hex.decodeHex(key.toCharArray());

		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		byte[] encrypted = cipher.doFinal(text.getBytes());

		StringBuffer sb = new StringBuffer(encrypted.length * 2);
		String hexNumber;

		for(int x=0; x<encrypted.length; x++){
			hexNumber = "0" + Integer.toHexString(0xff & encrypted[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}

		return sb.toString();
	}

	/**
	 * AES128 복호화
	 * @param text
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getDecrypted(String text, String key) throws Exception {
		if(StringUtils.isEmpty(text) || StringUtils.isEmpty(key)){
			return "";
		}

		byte[] keyBytes = Hex.decodeHex(key.toCharArray());

		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		//16진수 문자열을 byte로 변환
		byte[] byteArray = new byte[text.length() /2 ];

		for(int i=0; i<byteArray.length; i++){
			byteArray[i] = (byte) Integer.parseInt(text.substring(2 * i, 2*i+2), 16);
		}

		byte[] original = cipher.doFinal(byteArray);

		String originalStr = new String(original);

		return originalStr;

	}

    /**
     * AES256 암호화
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String data) throws Exception {
        byte[] dataBytes = data.getBytes("utf-8");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		String iv = key.substring(0, 16);

		cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(key), new IvParameterSpec(iv.getBytes()));

		byte[] encrypted = cipher.doFinal(dataBytes);
		String result = new String(Base64.encodeBase64(encrypted), "UTF-8");

		return result;
	}

    /**
     * AES256 복호화
     * @param key
     * @param str
     * @return
     * @throws Exception
     */
	public static String decrypt(String key, String str) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		String iv = key.substring(0, 16);
		cipher.init(Cipher.DECRYPT_MODE, getKeySpec(key), new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());

		String result = new String(cipher.doFinal(byteStr), "UTF-8");

		return result;
	}

    /**
     * 비밀번호 암호화
     * @param encKey
     * @param str
     * @return
     * @throws Exception
     */
	public static String getPwdOtpEncode(String encKey, String str) throws Exception {

        SimpleDateFormat formatTime = new SimpleDateFormat("yyyyMMddHH");
        formatTime.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        String currentTime = formatTime.format(new java.util.Date());

        SimpleDateFormat formatDay = new SimpleDateFormat("yyyyMMdd");
        formatDay.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        String currentDay = formatDay.format(new java.util.Date());

        String key = (Long.parseLong(currentTime) + Long.parseLong(currentDay)) + encKey;

        String encode = encrypt(key, str);
        String result = URLEncoder.encode(encode, "UTF-8");

        return result;
    }

    /**
     * 비밀번호 암호화
     * @param encKey
     * @param str
     * @return
     * @throws Exception
     */
	public static String getPwdEncode(String encKey, String str) throws Exception {

        String encode = encrypt(encKey, str);
        String result = URLEncoder.encode(encode, "UTF-8");

        return result;
    }

    /**
	 * SHA256 암호화
	 * @param str
	 * @return
	 */
	public static String encrytpedSHA256(String str){
		String returnStr = "";
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			returnStr = sb.toString();

		}catch(Exception e){
			e.printStackTrace();
			returnStr = null;
		}
		return returnStr;
	}

}
