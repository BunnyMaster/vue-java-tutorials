package com.spring.step3.utils;

import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.exception.AuthenticSecurityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JwtTokenUtil {

    /**
     * 使用默认主题，默认秘钥，自定义时间，创建集合形式token
     *
     * @param map 集合
     * @param day 过期时间
     * @return token
     */
    public static String createTokenWithMap(Map<String, Object> map, String subject, SecretKey key, Long day) {
        return Jwts.builder()
                .subject(subject)
                .signWith(key)
                .expiration(new Date(System.currentTimeMillis() + day))
                .claims(map)
                .id(UUID.randomUUID().toString())
                .compressWith(Jwts.ZIP.GZIP).compact();
    }

    /**
     * 使用自定义主题，自定义时间，创建集合形式token
     *
     * @param map     集合
     * @param subject 主题
     * @param time    过期时间
     * @return token
     */
    public static String createTokenWithMap(Map<String, Object> map, String subject, SecretKey key, Date time) {
        return Jwts.builder()
                .subject(subject)
                .expiration(time)
                .claims(map)
                .id(UUID.randomUUID().toString())
                .signWith(key)
                .compressWith(Jwts.ZIP.GZIP).compact();
    }

    /**
     * 创建集合形式token
     *
     * @param map     集合
     * @param subject 主题
     * @param day     过期时间
     * @return token
     */
    public static String createTokenWithMap(Map<String, Object> map, String subject, SecretKey key, Integer day) {
        return Jwts.builder()
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + day))
                .claims(map)
                .id(UUID.randomUUID().toString())
                .signWith(key)
                .compressWith(Jwts.ZIP.GZIP).compact();
    }

    /**
     * 根据用户名和ID创建token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param day      过期时间
     * @return token值
     */
    public static String createToken(Long userId, String username, String subject, SecretKey key, Long day) {
        return Jwts.builder()
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + day))
                .claim("userId", userId)
                .claim("username", username)
                .id(UUID.randomUUID().toString())
                .signWith(key)
                .compressWith(Jwts.ZIP.GZIP).compact();
    }

    /**
     * 根据用户名和ID创建token
     * 在载体中添加角色和权限
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param second   过期时间
     * @return token值
     */
    public static String createToken(Long userId, String username,
                                     List<String> roles, List<String> permissions,
                                     String subject, SecretKey key, Long second) {
        // 传进来的是秒，转成未来过期时间
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(second);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // 转成过期时间
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        return Jwts.builder()
                .subject(subject)
                .expiration(date)
                .claim("expiredTime", format)
                .claim("userId", userId)
                .claim("username", username)
                .claim("roles", roles)
                .claim("permissions", permissions)
                .id(UUID.randomUUID().toString())
                .signWith(key)
                .compressWith(Jwts.ZIP.GZIP).compact();
    }

    /**
     * 使用token获取map集合,使用默认秘钥
     *
     * @param token token
     * @return map集合
     */
    public static Map<String, Object> getMapByToken(String token, SecretKey key) {
        try {
            if (!StringUtils.hasText(token))
                throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);
            // 将 body 值转为map
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        } catch (Exception exception) {
            throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);
        }
    }

    private static String getSubjectByTokenHandler(String token, SecretKey key) {
        try {
            if (!StringUtils.hasText(token))
                throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            Claims body = claimsJws.getPayload();

            return body.getSubject();

        } catch (Exception exception) {
            throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);
        }
    }

    /**
     * 根据token获取主题
     *
     * @param token token
     * @return 主题
     */
    public static String getSubjectByToken(String token, SecretKey key) {
        return getSubjectByTokenHandler(token, key);
    }

    /**
     * 根据token获取用户ID
     *
     * @param token token
     * @return 用户ID
     */
    public static Long getUserId(String token, SecretKey key) {
        try {
            if (!StringUtils.hasText(token)) throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);

            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            Claims claims = claimsJws.getPayload();

            return Long.valueOf(String.valueOf(claims.get("userId")));
        } catch (Exception exception) {
            throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);
        }
    }

    /**
     * 根据token获取用户名
     *
     * @param token token
     * @return 用户名
     */
    public static String getUsername(String token, SecretKey key) {
        try {
            if (!StringUtils.hasText(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            Claims claims = claimsJws.getPayload();
            return (String) claims.get("username");
        } catch (Exception exception) {
            throw new AuthenticSecurityException(ResultCodeEnum.TOKEN_PARSING_FAILED);
        }
    }

    /**
     * 判断是否过期
     *
     * @param token token
     * @return 是否过期
     * @throws RuntimeException ⚠️解析失败和过期都属于异常类型
     */
    public static boolean isExpired(String token, SecretKey key) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            Date expiration = claimsJws.getPayload().getExpiration();

            return expiration != null && expiration.before(new Date());
        } catch (RuntimeException exception) {
            // ResultCodeEnum codeEnum = ResultCodeEnum.AUTHENTICATION_EXPIRED;
            // throw new IllegalArgumentException(codeEnum.getMessage(), exception);
            return true;
        }
    }
}