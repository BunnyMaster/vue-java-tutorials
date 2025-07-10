package com.spring.password;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StopWatch;

@Slf4j
public class PasswordBCryptTest {
    @Test
    void BCryptPasswordEncoderTest() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Create an encoder with strength 16
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode("myPassword");
        System.out.println(result);

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis() / 1000;
        System.out.println(totalTimeMillis);
    }

    @Test
    void Argon2PasswordEncoderTest() {
        // Create an encoder with all the defaults
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String result = encoder.encode("myPassword");
        System.out.println(result);
    }
}
