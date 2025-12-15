package com.spring.step3.security.service;

import com.spring.step3.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "jwt-token")
public class JwtTokenService {

    @Value("${jwtToken.secret}")
    public String secret;

    @Value("${jwtToken.subject}")
    public String subject;

    // private final SecretKey securityKey = Keys.hmacShaKeyFor("Bunny-Auth-Server-Private-SecretKey".getBytes(StandardCharsets.UTF_8));
    // JWT 的 秘钥
    @Value("${jwtToken.expired}")
    public Long expired;

    /**
     * 创建Token
     *
     * @param userId   用户Id
     * @param username 用户名
     * @return 令牌Token
     */
    public String createToken(Long userId, String username,
                              List<String> roles, List<String> permissions) {
        SecretKey key = getSecretKey();
        // return JwtTokenUtil.createToken(userId, username, subject, key, expired);
        return JwtTokenUtil.createToken(userId, username, roles, permissions, subject, key, expired);
    }

    /**
     * 获取安全密钥
     *
     * @return {@link SecretKey}
     */
    private SecretKey getSecretKey() {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }

    /**
     * 根据Token获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        SecretKey secretKey = getSecretKey();
        return JwtTokenUtil.getUsername(token, secretKey);
    }

    /**
     * 根据Token获取用户Id
     *
     * @param token 令牌
     * @return 用户Id
     */
    public Long getUserIdFromToken(String token) {
        SecretKey secretKey = getSecretKey();
        return JwtTokenUtil.getUserId(token, secretKey);
    }

    /**
     * 根据Token获取用户Id
     *
     * @param token 令牌
     * @return 用户Id
     */
    public Map<String, Object> getMapByToken(String token) {
        SecretKey secretKey = getSecretKey();
        return JwtTokenUtil.getMapByToken(token, secretKey);
    }

    /**
     * 判断当前Token是否国企
     *
     * @param token 令牌
     * @return 是否国企
     */
    public boolean isExpired(String token) {
        SecretKey secretKey = getSecretKey();
        return JwtTokenUtil.isExpired(token, secretKey);
    }
}
