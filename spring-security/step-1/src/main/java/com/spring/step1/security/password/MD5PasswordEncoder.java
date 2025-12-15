package com.spring.step1.security.password;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.HexFormat;

/**
 * <h1>MD5密码编码器实现</h1>
 *
 * <strong>安全警告：</strong>此类使用MD5算法进行密码哈希，已不再安全，不推荐用于生产环境。
 *
 * <p>MD5算法因其计算速度快且易受彩虹表攻击而被认为不安全。即使密码哈希本身是单向的，
 * 但现代计算能力使得暴力破解和预先计算的彩虹表攻击变得可行。</p>
 *
 * <p>Spring Security推荐使用BCrypt、PBKDF2、Argon2或Scrypt等自适应单向函数替代MD5。</p>
 *
 * @see PasswordEncoder
 * 一般仅用于遗留系统兼容，新系统应使用更安全的密码编码器
 */
public class MD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("原始密码不能为null");
        }

        byte[] md5Digest = DigestUtils.md5Digest(rawPassword.toString().getBytes());
        return HexFormat.of().formatHex(md5Digest);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("原始密码不能为null");
        }

        if (!StringUtils.hasText(encodedPassword)) {
            return false;
        }

        return encodedPassword.equalsIgnoreCase(encode(rawPassword));
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        // MD5已不安全，始终返回true建议升级到更安全的算法
        return true;
    }
}