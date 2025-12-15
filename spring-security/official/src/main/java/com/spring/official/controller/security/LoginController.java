package com.spring.official.controller.security;

import com.spring.official.domain.dto.security.LoginRequest;
import com.spring.official.domain.vo.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login接口", description = "登录接口")
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @Operation(summary = "登录接口", description = "系统登录接口")
    @PostMapping("login")
    public Result<Authentication> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        return Result.success(authenticationResponse);
    }
}