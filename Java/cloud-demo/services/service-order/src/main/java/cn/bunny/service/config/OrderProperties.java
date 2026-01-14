package cn.bunny.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
// 配置批量绑定无需 @RefreshScope 实现动态刷新
@ConfigurationProperties(prefix = "order")
@Data
public class OrderProperties {

    private String timeout;

    // 中划线写法会自动映射为小驼峰
    private String autoConfirm;

    private String dbUrl;

}
