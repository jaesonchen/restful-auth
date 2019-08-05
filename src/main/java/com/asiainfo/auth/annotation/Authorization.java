package com.asiainfo.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**   
 * @Description: Authorization注解的方法，在springmvc拦截器里检查是否已登陆、token是否已失效（redis缓存的token）；验证失败返回401，验证成功将token对应的principal放入request，以便后续的Principal注解使用
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午2:28:49
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {

}
