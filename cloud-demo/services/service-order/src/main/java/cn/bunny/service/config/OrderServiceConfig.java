package cn.bunny.service.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderServiceConfig {

    @Bean
    public Retryer feignRetryer() {
        // 重试间隔100ms，最大间隔1s，最大尝试次数3次
        // return new Retryer.Default();
        return new Retryer.Default(100, 1000, 3);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 配置日志全级
     *
     * @return Logger级别
     */
    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }
}
