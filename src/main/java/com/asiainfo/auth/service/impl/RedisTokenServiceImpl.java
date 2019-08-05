package com.asiainfo.auth.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.auth.model.Payload;
import com.asiainfo.auth.service.TokenService;
import com.asiainfo.auth.util.Constants;
import com.asiainfo.auth.util.TokenUtil;

/**   
 * @Description: 基于Redis的Token管理
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午3:43:04
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Component
public class RedisTokenServiceImpl implements TokenService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${token.secret}")
    private String secret;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Override
    public String create(String userId) {
        
        logger.info("create token, userId={}", userId);
        long issued = System.currentTimeMillis();
        long expire = issued + Constants.TOKEN_EXPIRES_MINUTE * 60 * 1000L;
        Payload payload = new Payload(Constants.APPID, userId, issued, expire);
        String token = TokenUtil.createToken(payload, secret);
        logger.info("create token, token={}", token);
        return token;
    }

    @Override
    public String refresh(String token) {
        
        logger.info("refresh token, token={}", token);
        Payload payload = TokenUtil.fromToken(token);
        long expireInSecond = (payload.getExpire() - System.currentTimeMillis()) / 1000;
        String[] tokens = token.split("\\.");
        long validTime = System.currentTimeMillis() + 60 * 1000L;
        if (expireInSecond > 0) {
            logger.info("refresh token, relay token redis key={}", Constants.TOKEN_KEY_PREFIX + tokens[1]);
            // 把废弃但过期时间还没到的旧凭证存入redis，保留1分钟的过渡时间，以免并发调度出现不可用
            redisTemplate.boundValueOps(Constants.TOKEN_KEY_PREFIX + tokens[1]).set(String.valueOf(validTime), expireInSecond, TimeUnit.SECONDS);
        }
        // 更新凭证的过期时间
        payload.setExpire(System.currentTimeMillis() + Constants.TOKEN_EXPIRES_MINUTE * 60 * 1000L);
        String newToken = TokenUtil.createToken(payload, secret);
        logger.info("refresh token, new token={}", newToken);
        return newToken;
    }
    
    @Override
    public void discard(String token) {
        
        logger.info("discard token, token={}", token);
        Payload payload = TokenUtil.fromToken(token);
        long expireInSecond = (payload.getExpire() - System.currentTimeMillis()) / 1000;
        String[] tokens = token.split("\\.");
        if (expireInSecond > 0) {
            logger.info("discard token, relay token redis key={}", Constants.TOKEN_KEY_PREFIX + tokens[1]);
            // 把废弃但过期时间还没到的凭证存入redis，丢弃的令牌不保留过渡时间
            redisTemplate.boundValueOps(Constants.TOKEN_KEY_PREFIX + tokens[1]).set("0", expireInSecond, TimeUnit.SECONDS);
        }
    }
    
    @Override
    public boolean check(String token) {
        
        logger.info("check token, token={}", token);
        // 检查token是否匹配签名
        if (!TokenUtil.checkToken(token, this.secret)) {
            logger.info("check token, token not match signature!");
            return false;
        }
        
        Payload payload = TokenUtil.fromToken(token);
        if (null == payload) {
            logger.info("check token, payload invalid!");
            return false;
        }
        // 令牌过期时间检查
        if (payload.getExpire() < System.currentTimeMillis()) {
            logger.info("check token fail, token expire!");
            return false;
        }
        
        String[] tokens = token.split("\\.");
        // 从redis读取失效、丢弃的令牌记录
        String timeStr = redisTemplate.boundValueOps(Constants.TOKEN_KEY_PREFIX + tokens[1]).get();
        // 没有失效记录说明令牌有效
        if (StringUtils.isEmpty(timeStr)) {
            logger.info("check token success!");
            return true;
        }
        // 判断是否还在令牌过渡期
        long relayTime = Long.parseLong(timeStr);
        logger.info("check token {}!", relayTime > System.currentTimeMillis() ? "success(relay time)" : "invalid");
        return relayTime > System.currentTimeMillis();
    }
}
