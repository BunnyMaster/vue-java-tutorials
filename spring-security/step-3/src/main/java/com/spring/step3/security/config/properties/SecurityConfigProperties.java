package com.spring.step3.security.config.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security-path")
@Schema(name = "SecurityPathsProperties对象", description = "路径忽略和认证")
public class SecurityConfigProperties {

    @Schema(name = "noAuthPaths", description = "不用认证的路径")
    public List<String> noAuthPaths;

    @Schema(name = "securedPaths", description = "需要认证的路径")
    public List<String> securedPaths;

    @Schema(name = "允许的角色或权限", description = "允许的角色或权限")
    public List<String> adminAuthorities;

}
