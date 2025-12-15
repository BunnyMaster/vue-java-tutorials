// package com.spring.step3.security.manger.demo1;
//
// import com.spring.step3.security.properties.SecurityConfigProperties;
// import lombok.RequiredArgsConstructor;
// import org.aopalliance.intercept.MethodInvocation;
// import org.springframework.core.annotation.AnnotationUtils;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.authorization.AuthorizationDecision;
// import org.springframework.security.authorization.AuthorizationManager;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.stereotype.Component;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.function.Supplier;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
//
// /**
//  * 处理方法调用前的授权检查
//  * check()方法接收的是MethodInvocation对象，包含即将执行的方法调用信息
//  * 用于决定是否允许执行某个方法
//  * 这是传统的"前置授权"模式
//  */
// @Component
// @RequiredArgsConstructor
// public class PreAuthorizationManager implements AuthorizationManager<MethodInvocation> {
//
//     private final SecurityConfigProperties securityConfigProperties;
//
//     @Override
//     public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, MethodInvocation methodInvocation) {
//         Authentication authentication = authenticationSupplier.get();
//
//         // 如果方法有 @PreAuthorize 注解，会先到这里
//         if (authentication == null || !authentication.isAuthenticated()) {
//             return new AuthorizationDecision(false);
//         }
//
//         // 检查权限
//         boolean granted = hasPermission(authentication, methodInvocation);
//         return new AuthorizationDecision(granted);
//     }
//
//     private boolean hasPermission(Authentication authentication, MethodInvocation methodInvocation) {
//         PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), PreAuthorize.class);
//         if (preAuthorize == null) {
//             return true; // 没有注解默认放行
//         }
//
//         String expression = preAuthorize.value();
//         // 解析表达式中的权限要求
//         List<String> requiredAuthorities = extractAuthoritiesFromExpression(expression);
//
//         // 获取配置的admin权限
//         List<String> adminAuthorities = securityConfigProperties.getAdminAuthorities();
//
//         return authentication.getAuthorities().stream()
//                 .map(GrantedAuthority::getAuthority)
//                 .anyMatch(auth ->
//                         adminAuthorities.contains(auth) ||
//                                 requiredAuthorities.contains(auth)
//                 );
//     }
//
//     private List<String> extractAuthoritiesFromExpression(String expression) {
//         List<String> authorities = new ArrayList<>();
//
//         // 处理 hasAuthority('permission') 格式
//         Pattern hasAuthorityPattern = Pattern.compile("hasAuthority\\('([^']+)'\\)");
//         Matcher hasAuthorityMatcher = hasAuthorityPattern.matcher(expression);
//         while (hasAuthorityMatcher.find()) {
//             authorities.add(hasAuthorityMatcher.group(1));
//         }
//
//         // 处理 hasRole('ROLE_XXX') 格式 (Spring Security 会自动添加 ROLE_ 前缀)
//         Pattern hasRolePattern = Pattern.compile("hasRole\\('([^']+)'\\)");
//         Matcher hasRoleMatcher = hasRolePattern.matcher(expression);
//         while (hasRoleMatcher.find()) {
//             authorities.add(hasRoleMatcher.group(1));
//         }
//
//         // 处理 hasAnyAuthority('perm1','perm2') 格式
//         Pattern hasAnyAuthorityPattern = Pattern.compile("hasAnyAuthority\\(([^)]+)\\)");
//         Matcher hasAnyAuthorityMatcher = hasAnyAuthorityPattern.matcher(expression);
//         while (hasAnyAuthorityMatcher.find()) {
//             String[] perms = hasAnyAuthorityMatcher.group(1).split(",");
//             for (String perm : perms) {
//                 authorities.add(perm.trim().replaceAll("'", ""));
//             }
//         }
//
//         // 处理 hasAnyRole('role1','role2') 格式
//         Pattern hasAnyRolePattern = Pattern.compile("hasAnyRole\\(([^)]+)\\)");
//         Matcher hasAnyRoleMatcher = hasAnyRolePattern.matcher(expression);
//         while (hasAnyRoleMatcher.find()) {
//             String[] roles = hasAnyRoleMatcher.group(1).split(",");
//             for (String role : roles) {
//                 authorities.add(role.trim().replaceAll("'", ""));
//             }
//         }
//
//         return authorities;
//     }
// }