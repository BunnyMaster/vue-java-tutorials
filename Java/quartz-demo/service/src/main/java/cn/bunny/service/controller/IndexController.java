package cn.bunny.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "访问首页内容")
@RestController
@RequestMapping("/")
public class IndexController {
    @Operation(summary = "访问首页", description = "访问首页")
    @GetMapping("")
    public String index() {
        return "欢迎访问 Bunny Java Template，欢迎去Gitee：https://gitee.com/BunnyBoss/java_single.git";
    }
}
