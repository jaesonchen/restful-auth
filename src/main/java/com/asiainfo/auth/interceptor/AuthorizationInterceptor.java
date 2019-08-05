package com.asiainfo.auth.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.asiainfo.auth.annotation.Authorization;
import com.asiainfo.auth.model.Payload;
import com.asiainfo.auth.service.TokenService;
import com.asiainfo.auth.util.Constants;
import com.asiainfo.auth.util.TokenUtil;

/**   
 * @Description: Authorization验证拦截器，用于验证标注了Authorization注解的restful方法，通过header传递的token是否有效
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午2:45:03
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private TokenService tokenService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 不是映射到方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 方法是否标有Authorization注解
        if (method.getAnnotation(Authorization.class) != null) {
            // 从header中得到token字符串
            String token = request.getHeader(Constants.TOKEN_HEADER);
            logger.info("@Authorization token={}", token);
            // 验证token
            if (tokenService.check(token)) {
                // 转换为Payload
                Payload payload = TokenUtil.fromToken(token);
                logger.info("@Authorization payload={}", payload);
                // token验证成功，将token对应的principal存在request中，便于之后的方法参数注入
                request.setAttribute(Constants.PAYLOAD_USERID, payload.getUserId());
                return true;
            } else {
                // token验证失败，返回401错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        
        // 没有注解不需要验证，直接返回
        return true;
    }
}
