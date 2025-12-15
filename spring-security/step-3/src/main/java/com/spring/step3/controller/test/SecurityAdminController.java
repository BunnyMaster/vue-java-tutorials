package com.spring.step3.controller.test;

import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.security.annotation.IsAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ADMIN接口", description = "只要包含 Admin 角色都可以访问")
@Slf4j
@RestController
@RequestMapping("/api/security/admin")
public class SecurityAdminController {

    @IsAdmin
    @Operation(summary = "拥有 IsAdmin 的角色可以访问", description = "当前用户拥有 IsAdmin 角色可以访问这个接口")
    @GetMapping("role-user")
    public Result<String> roleUser() {
        return Result.success();
    }

    @IsAdmin
    @Operation(summary = "拥有 IsAdmin 的角色可以访问", description = "当前用户拥有 IsAdmin 角色可以访问这个接口")
    @GetMapping("upper-user")
    public Result<String> upperUser() {
        String data = "是区分大小写的";
        return Result.success(data);
    }

    @IsAdmin
    @Operation(summary = "拥有 IsAdmin 的角色可以访问", description = "当前用户拥有 IsAdmin 角色可以访问这个接口")
    @GetMapping("lower-user")
    public Result<String> lowerUser() {
        String data = "如果是大写，但是在这里是小写无法访问";
        return Result.success(data);
    }

}
