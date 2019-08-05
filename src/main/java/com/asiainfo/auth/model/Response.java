package com.asiainfo.auth.model;

import java.io.Serializable;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午3:14:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private Object data;
    
    public Response(String code, String message) {
        this(code, message, null);
    }
    public Response(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    
    public static Response index() {
        return new Response("200", "index success!");
    }
    public static Response success() {
        return new Response("200", "success");
    }
    public static Response success(Object data) {
        return new Response("200", "success", data);
    }
    public static Response loginSuccess(Object data) {
        return new Response("200", "login success!", data);
    }
    public static Response loginError() {
        return new Response("404", "login error, username/password invalid!");
    }
    public static Response logoutSuccess() {
        return new Response("200", "logout success!");
    }
    
    @Override
    public String toString() {
        return "Response [code=" + code + ", message=" + message + ", data=" + data + "]";
    }
}
