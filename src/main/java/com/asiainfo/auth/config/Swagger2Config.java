package com.asiainfo.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**   
 * @Description: swagger-ui配置
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午4:31:33
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {
    
    // swagger-ui.html文档展示页，还必须注入swagger资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.asiainfo.auth.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Spring RESTful Authorization Api")
                .description("SpringBoot 集成 Swagger2 构建RESTful API")
                // 服务条款网址
                .termsOfServiceUrl("https://github.com/jaesonchen")
                // 创建人
                .contact(new Contact("jaesonchen", "https://github.com/jaesonchen", "jaesonchen@163.com"))
                .version("1.0")
                .build();
    }
}
