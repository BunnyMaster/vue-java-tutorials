package com.spring.step1.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Knife4jConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI openAPI() {
        String url = "http://localhost:" + port;

        // 作者等信息
        Contact contact = new Contact().name("Bunny").email("1319900154@qq.com").url(url);
        // 使用协议
        License license = new License().name("MIT").url("https://mit-license.org");
        // 相关信息
        Info info = new Info().title("Bunny-Admin")
                .contact(contact).license(license)
                .description("Bunny代码生成器")
                .summary("Bunny的代码生成器")
                .termsOfService(url)
                .version("v1.0.0");

        return new OpenAPI().info(info).externalDocs(new ExternalDocumentation());
    }

    @Bean
    public GroupedOpenApi all() {
        return GroupedOpenApi.builder().group("全部请求接口").pathsToMatch("/api/**").build();
    }

    @Bean
    public GroupedOpenApi dept() {
        return GroupedOpenApi.builder().group("部门请求接口").pathsToMatch("/api/dept/**").build();
    }

    @Bean
    public GroupedOpenApi normal() {
        return GroupedOpenApi.builder().group("访客请求接口").pathsToMatch("/api/anonymous/**").build();
    }

    @Bean
    public GroupedOpenApi user() {
        return GroupedOpenApi.builder().group("用户请求接口").pathsToMatch("/api/user/**").build();
    }

    @Bean
    public GroupedOpenApi security() {
        return GroupedOpenApi.builder().group("security接口").pathsToMatch("/api/security/**").build();
    }
}
