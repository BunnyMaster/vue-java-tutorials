package cn.bunny.common.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 跨域配置
     *
     * @param registry 跨域注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 是否发送Cookies
                .allowCredentials(true)
                // 放行哪些原始域
                .allowedOriginPatterns("*").allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*").exposedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludeList = {"/api/checkCode", "/api/sendEmailCode", "/api/register", "/api/login", "/api/article/loadArticle/**"};
        log.info("WebMvcConfiguration===>开始注册自定义拦截器...");
        // TODO 如果想使用普通JWT可以使用这个，不使用 SpringSecurity6
        // registry.addInterceptor(userTokenInterceptor).addPathPatterns("/api/**").excludePathPatterns(excludeList);
    }
}
