package com.asiainfo.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**   
 * @Description: 注解restful方法参数，与Authorization配合使用，从request里取出Authorization解析的token principal，从数据库读取对应信息，解析注入方法参数
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午2:32:05
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Principal {

}
