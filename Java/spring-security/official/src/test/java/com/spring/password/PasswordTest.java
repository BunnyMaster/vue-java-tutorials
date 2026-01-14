package com.spring.password;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordTest {

    @Test
    void passwordEncoderTest() {
        // 只是便捷机制，不适用于生产环境
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String encode = passwordEncoder.encode("123456");
        log.info("PasswordEncoder 加密密码：{}", encode);

        boolean matches = passwordEncoder.matches("123456", encode);
        log.info("PasswordEncoder 是否匹配：{}", matches);
    }

    @Test
    void UserDetailTest() {
        UserDetails user = User.builder()
                .username("user")
                .password("123456")
                .roles("user")
                .build();
        UserDetails password = User.withUserDetails(user).password("123456").build();
        System.out.println(password.getPassword());// 123456
    }

    @Test
    void EncryptorsTest() {
        String salt = KeyGenerators.string().generateKey(); // generates a random 8-byte salt that is then hex-encoded
        String string = "password";
        BytesEncryptor bytesEncryptor = Encryptors.stronger(string, salt);
        byte[] encrypted = bytesEncryptor.encrypt(string.getBytes());
        byte[] decrypt = bytesEncryptor.decrypt(string.getBytes());

        TextEncryptor password = Encryptors.text(string, salt);
        String encrypt = password.encrypt(string);
        String decrypted = password.decrypt(encrypt);
        System.out.println(encrypt);
        System.out.println(decrypted);
    }

    @Test
    void UserDetailsTest() {
        // withDefaultPasswordEncoder 是不安全的不适用于生产环境
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("user")
                .build();
        System.out.println(user.getPassword());
    }

}
