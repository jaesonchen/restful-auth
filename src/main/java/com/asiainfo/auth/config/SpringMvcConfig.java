package com.asiainfo.auth.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.asiainfo.auth.interceptor.AuthorizationInterceptor;
import com.asiainfo.auth.resolver.PrincipalMethodArgumentResolver;

/**   
 * @Description: 注册验证拦截器和参数解析器
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午5:08:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationInterceptor interceptor;
    
    @Autowired
    private PrincipalMethodArgumentResolver resolver;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(resolver);
    }
}
