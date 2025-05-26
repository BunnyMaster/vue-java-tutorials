package cn.bunny.service.feign;

import cn.bunny.model.order.bean.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BunnyFeignClientTest {

    @Autowired
    private BunnyFeignClient bunnyFeignClient;

    @Test
    void test() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("bunny");
        loginDto.setPassword("admin123");
        loginDto.setType("default");
        String login = bunnyFeignClient.login(loginDto);

        System.out.println(login);
    }

}