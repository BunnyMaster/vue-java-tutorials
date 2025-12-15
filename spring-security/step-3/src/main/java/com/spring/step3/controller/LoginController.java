package com.spring.step3.controller;

import com.spring.step3.domain.dto.LoginDto;
import com.spring.step3.domain.vo.LoginVo;
import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.service.user.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录接口")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "用户登录", description = "用户登录")
    @PostMapping("login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo vo = loginService.login(loginDto);
        return Result.success(vo);
    }

}
