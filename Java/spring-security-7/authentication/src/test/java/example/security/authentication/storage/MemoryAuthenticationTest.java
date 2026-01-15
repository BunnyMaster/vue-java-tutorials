package example.security.authentication.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class MemoryAuthenticationTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemoryAuthentication memoryAuthentication;

    @Test
    void contextLoads() {
        assertNotNull(memoryAuthentication);
        assertNotNull(userDetailsService);
    }

    @Test
    void testUser1Exists() {
        UserDetails user = userDetailsService.loadUserByUsername("user1_MemoryAuthentication");

        assertNotNull(user);
        assertEquals("user1_MemoryAuthentication", user.getUsername());
        assertTrue(user.getPassword().startsWith("{bcrypt}"));

        // 验证角色
        assertTrue(user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertFalse(user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testAdmin1Exists() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin1_MemoryAuthentication");

        assertNotNull(admin);
        assertEquals("admin1_MemoryAuthentication", admin.getUsername());
        assertTrue(admin.getPassword().startsWith("{bcrypt}"));

        // 验证角色
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testNonExistentUser() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testUser2Exists_ByCreatingBeanDirectly() {
        // 直接调用方法获取第二个UserDetailsService
        UserDetailsService users2Service = memoryAuthentication.users2();

        UserDetails user = users2Service.loadUserByUsername("user2_MemoryAuthentication");

        assertNotNull(user);
        assertEquals("user2_MemoryAuthentication", user.getUsername());
        // 注意：这里使用的是默认密码编码器，密码是"password"的编码形式

        // 验证角色
        assertTrue(user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testAdmin2Exists_ByCreatingBeanDirectly() {
        UserDetailsService users2Service = memoryAuthentication.users2();

        UserDetails admin = users2Service.loadUserByUsername("admin2_MemoryAuthentication");

        assertNotNull(admin);
        assertEquals("admin2_MemoryAuthentication", admin.getUsername());

        // 验证角色
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
}