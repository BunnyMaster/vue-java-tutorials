import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderTest {
    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new TestingAuthenticationToken("username", "password", "ROLE_USER");
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    /**
     * 测试 SecurityContextHolder 中 Authentication 结构
     */
    @Test
    void test_security_context_holder() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        String jsonString = JSON.toJSONString(authentication);
        System.out.println(jsonString);
    }
}
