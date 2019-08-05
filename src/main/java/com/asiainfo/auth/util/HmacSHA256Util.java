package com.asiainfo.auth.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年6月28日 下午10:39:43
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class HmacSHA256Util {

    public static String encrypt(String data, String secret) throws Exception {
        return encrypt(data.getBytes("utf-8"), secret.getBytes("utf-8"));
    }
    
    public static String encrypt(byte[] data, String secret) throws Exception {
        return encrypt(data, secret.getBytes("utf-8"));
    }
    
    public static String encrypt(byte[] data, byte[] secret) throws Exception {
        
        Mac hs256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret, "HmacSHA256");
        hs256.init(secretKey);
        return BytesToHex.bytesToHex(hs256.doFinal(data));
    }
}
