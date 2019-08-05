package com.asiainfo.auth.util;

import java.util.Base64;

/**
 * java8 BASE64 url safe算法实现加解密，+  /  被替换 = 被去除
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:37:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Base64URLUtil {

	/**
	 * url加密
	 * 
	 * @param data
	 * @return
	 */
    public static String base64UrlEncrypt(byte[] data) {
        return Base64.getUrlEncoder().encodeToString(data).replaceAll("=", "");
    }
    
    /**
     * url解密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] base64UrlDecrypt(String data) throws Exception {
        return Base64.getUrlDecoder().decode(data);
    }
}
