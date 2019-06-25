package com.qihsoft.webdev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Qihsoft on 2017/9/7.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Value("${swagger2.enable}")
    private boolean swagger2Enable;

    @Bean
    public Docket systemApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swagger2Enable)
                .groupName("admin-api")
                .apiInfo(systemInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qihsoft.webdev.api.admin"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket unifiedApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swagger2Enable)
                .groupName("unified-api")
                .apiInfo(unifiedInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qihsoft.webdev.api.unified"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo systemInfo() {
        return new ApiInfoBuilder().title("校园服务后台端 授权API管理")
                .description("  ")
                .termsOfServiceUrl("https://wechat.gxun.club")
                .contact("qihsoft")
                .version("0.3.3")
                .build();
    }

    private ApiInfo unifiedInfo() {
        return new ApiInfoBuilder().title("校园服务后台端  通用API管理")
                .description("  ")
                .termsOfServiceUrl("https://wechat.gxun.club")
                .contact("qihsoft")
                .version("0.3.3")
                .build();
    }
}
