package com.spring.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.util.Arrays;

public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        String rawPasswordString = rawPassword.toString();
        byte[] md5Digest = DigestUtils.md5Digest(rawPasswordString.getBytes());
        return Arrays.toString(md5Digest);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
