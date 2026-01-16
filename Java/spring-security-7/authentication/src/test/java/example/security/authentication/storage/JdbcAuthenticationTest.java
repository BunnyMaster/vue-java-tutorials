package example.security.authentication.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class JdbcAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void testLoginSuccess() throws Exception {
        String requestBody = "{\"username\":\"user1_MemoryAuthentication\",\"password\":\"password\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginFailure() throws Exception {
        String requestBody = "{\"username\":\"user\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedResourceWithoutAuth() throws Exception {
        // 假设有一个需要认证的接口
        mockMvc.perform(post("/api/protected"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testLoadUserByUsername() {
        // 测试用户存在
        UserDetails user = userDetailsService.loadUserByUsername("user");
        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertTrue(user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        // 测试管理员存在
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        // 测试不存在的用户
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testPasswordEncoding() {
        UserDetails user = userDetailsService.loadUserByUsername("user1_MemoryAuthentication");
        // 密码应该以 {bcrypt} 开头
        assertTrue(user.getPassword().startsWith("{bcrypt}"));
    }
}