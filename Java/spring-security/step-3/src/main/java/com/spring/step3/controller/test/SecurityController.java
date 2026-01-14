package com.spring.step3.controller.test;

import com.spring.step3.domain.vo.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "NORMAl接口", description = "测试用的NORMAl接口")
@Slf4j
@RestController
@RequestMapping("/api/security/normal")
public class SecurityController {

    @PreAuthorize("hasAuthority('role::read')")
    @Operation(summary = "拥有 role:read 的角色可以访问", description = "当前用户拥有 role:read 角色可以访问这个接口")
    @GetMapping("role-user")
    public Result<String> roleUser() {
        return Result.success();
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "拥有 USER 的角色可以访问", description = "当前用户拥有 USER 角色可以访问这个接口")
    @GetMapping("upper-user")
    public Result<String> upperUser() {
        String data = "是区分大小写的";
        return Result.success(data);
    }

    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = "拥有 USER 的角色可以访问", description = "当前用户拥有 USER 角色可以访问这个接口")
    @GetMapping("lower-user")
    public Result<String> lowerUser() {
        String data = "如果是大写，但是在这里是小写无法访问";
        return Result.success(data);
    }

    @PostAuthorize("returnObject.data == authentication.name")
    @Operation(summary = "测试使用返回参数判断权限", description = "测试使用返回参数判断权限 用户拥有 role::read 可以访问这个接口")
    @GetMapping("test-post-authorize")
    public Result<String> testPostAuthorize() {
        log.info("方法内容已经执行。。。");
        String data = "Bunny";
        return Result.success(data);
    }
}
