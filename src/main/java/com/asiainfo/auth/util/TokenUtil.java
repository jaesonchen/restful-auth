package com.asiainfo.auth.util;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.asiainfo.auth.model.Payload;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年6月28日 下午11:03:05
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class TokenUtil {

    /**
     * @Description: 使用Payload创建一个包含签名的token
     *
     * @param payload
     * @param secret
     * @return
     */
    public static String createToken(Payload payload, String secret) {
        
        if (null == payload) {
            return null;
        }
        
        try {
            StringBuilder token = new StringBuilder();
            // to json
            String json = JSON.toJSONString(payload);
            // encrypt json
            String body = Base64URLUtil.base64UrlEncrypt(json.getBytes("utf-8"));
            token.append(body).append(".");
            // signature
            token.append(HmacSHA256Util.encrypt(body, secret));
            return token.toString();
        } catch (Exception ex) {
            // ignore
        }
        return null;
    }
    
    /**
     * @Description: 检查token和签名是否匹配
     *
     * @param token
     * @param secret
     * @return
     */
    public static boolean checkToken(String token, String secret) {
        
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        
        String[] tokens = token.split("\\.");
        if (tokens.length != 2) {
            return false;
        }
        
        try {
            // check signature
            String signature = HmacSHA256Util.encrypt(tokens[0], secret);
            return signature.equals(tokens[1]);
        } catch (Exception ex) {
            // ignore
        }
        return false;
    }
    
    /**
     * @Description: 从签名中解析出Payload
     *
     * @param token
     * @return
     */
    public static Payload fromToken(String token) {
        
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        
        String[] tokens = token.split("\\.");
        if (tokens.length != 2) {
            return null;
        }
        
        try {
            // decrypt json
            String json = new String(Base64URLUtil.base64UrlDecrypt(tokens[0]), "utf-8");
            // json to Payload
            return JSON.parseObject(json, Payload.class);
        } catch (Exception ex) {
            // ignore
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        
        Payload payload = new Payload("restful", "chenzq", 11111, 22222);
        String token = createToken(payload, "ddddddddddddd");
        System.out.println(token);
        System.out.println(checkToken(token, "ddddddddddddd"));
        System.out.println(fromToken(token));
    }
}
