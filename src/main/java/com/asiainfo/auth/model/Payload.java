package com.asiainfo.auth.model;

import java.io.Serializable;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午2:46:18
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Payload implements Serializable {

    private static final long serialVersionUID = 1L;

    // 授权模块
    private String appId;
    // 权限主体
    private String userId;
    // 签发时间
    private long issued;
    // 过期时间
    private long expire;

    
    public Payload() {}
    public Payload(String appId, String userId, long issued, long expire) {
        this.appId = appId;
        this.userId = userId;
        this.issued = issued;
        this.expire = expire;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getIssued() {
        return issued;
    }
    public void setIssued(long issued) {
        this.issued = issued;
    }
    public long getExpire() {
        return expire;
    }
    public void setExpire(long expire) {
        this.expire = expire;
    }
    @Override
    public String toString() {
        return "Payload [appId=" + appId + ", userId=" + userId + ", issued=" + issued + ", expire=" + expire + "]";
    }
}
