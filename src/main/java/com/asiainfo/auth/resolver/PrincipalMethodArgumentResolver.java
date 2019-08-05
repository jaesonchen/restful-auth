package com.asiainfo.auth.resolver;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.asiainfo.auth.annotation.Principal;
import com.asiainfo.auth.model.User;
import com.asiainfo.auth.repository.UserRepository;
import com.asiainfo.auth.util.Constants;

/**   
 * @Description: Principal 注解的方法参数注入解析
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午3:26:54
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Component
public class PrincipalMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserRepository repository;
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        // 只解析参数类型是User并且有Principal注解的方法参数
        if (parameter.getParameterType().isAssignableFrom(User.class) 
                && parameter.hasParameterAnnotation(Principal.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        
        // 取出鉴权时存入的principal
        String userId = (String) webRequest.getAttribute(Constants.PAYLOAD_USERID, RequestAttributes.SCOPE_REQUEST);
        if (!StringUtils.isEmpty(userId)) {
            // 从数据库中查询并返回
            return repository.findByUsername(userId);
        }
        // 解析失败时，抛出异常
        throw new ServletException(Constants.PAYLOAD_USERID);
    }
}
