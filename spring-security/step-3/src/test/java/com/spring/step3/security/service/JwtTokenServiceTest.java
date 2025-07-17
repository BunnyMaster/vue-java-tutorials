package com.spring.step3.security.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

class JwtTokenServiceTest {

    @Test
    void createToken() {
        byte[] secretBytes = "aVeryLongAndSecureRandomStringWithAtLeast32Characters".getBytes(StandardCharsets.UTF_8);
        SecretKeySpec hmacSHA256 = new SecretKeySpec(secretBytes, "HmacSHA256");

        String token = Jwts.builder()
                .subject("")
                .expiration(new Date(System.currentTimeMillis() - 60 * 60 * 60 * 24 * 7))

                .id(UUID.randomUUID().toString())
                .signWith(hmacSHA256)
                .compressWith(Jwts.ZIP.GZIP).compact();

        System.out.println(token);
    }

}