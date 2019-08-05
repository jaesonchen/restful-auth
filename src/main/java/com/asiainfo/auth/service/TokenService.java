package com.asiainfo.auth.service;

/**   
 * @Description: Token操作接口
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午2:45:52
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface TokenService {

    /**
     * @Description: 为指定用户创建一个令牌
     *
     * @param userId
     * @return
     */
    public String create(String userId);
    
    /**
     * @Description: 刷新令牌
     *
     * @param token
     * @return
     */
    public String refresh(String token);
    
    /**
     * @Description: 丢弃令牌
     *
     * @param token
     */
    public void discard(String token);
    
    
    /**
     * @Description: 检查令牌是否有效
     *
     * @param token
     * @return
     */
    public boolean check(String token);
}
