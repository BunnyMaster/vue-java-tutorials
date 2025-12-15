package cn.bunny.service.feign;

import cn.bunny.model.order.bean.LoginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "bunny-client", url = "http://bunny-web.site/api/user")
public interface BunnyFeignClient {

    @PostMapping("login")
    String login(@RequestBody LoginDto dtp);

}
