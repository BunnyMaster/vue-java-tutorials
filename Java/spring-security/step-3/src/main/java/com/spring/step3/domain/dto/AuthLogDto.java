package com.spring.step3.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AuthLogDTO对象", title = "系统授权日志表", description = "系统授权日志表的DTO对象")
public class AuthLogDto {

    @Schema(name = "eventType", title = "事件类型(GRANTED=授权成功,DENIED=授权拒绝)")
    private String eventType;

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "userId", title = "用户ID")
    private Long userId;

    @Schema(name = "requestIp", title = "请求IP")
    private String requestIp;

    @Schema(name = "requestMethod", title = "请求方法(GET,POST等)")
    private String requestMethod;

    @Schema(name = "requestUri", title = "请求URI")
    private String requestUri;

    @Schema(name = "className", title = "类名")
    private String className;

    @Schema(name = "methodName", title = "方法名")
    private String methodName;

    @Schema(name = "methodParams", title = "方法参数(JSON格式)")
    private String methodParams;

    @Schema(name = "requiredAuthority", title = "所需权限表达式")
    private String requiredAuthority;

    @Schema(name = "userAuthorities", title = "用户拥有的权限(JSON格式)")
    private String userAuthorities;

    @Schema(name = "decisionReason", title = "决策原因")
    private String decisionReason;

    @Schema(name = "exceptionMessage", title = "异常信息")
    private String exceptionMessage;

    @Schema(name = "isDeleted", title = "删除标志(0=未删除 1=已删除)")
    private Boolean isDeleted;

}