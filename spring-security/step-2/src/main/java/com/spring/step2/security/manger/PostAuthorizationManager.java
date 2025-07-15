package com.spring.step2.security.manger;

/**
 * 处理方法调用后的授权检查
 * check()方法接收的是MethodInvocationResult对象，包含已执行方法的结果
 * 用于决定是否允许返回某个方法的结果(后置过滤)
 * 这是Spring Security较新的"后置授权"功能
 */
// @Component
// public class PostAuthorizationManager implements AuthorizationManager<MethodInvocationResult> {
//
//     /**
//      * 这里两个实现方法按照Security官方要求进行实现
//      * <h4>类说明：</h4>
//      * 下面的实现是对方法执行前进行权限校验的判断
//      * <pre>
//      *     <code>AuthorizationManager &ltMethodInvocation></code>
//      * </pre>
//      * 下面的这个是对方法执行后对权限的判断
//      * <pre>
//      *     <code>AuthorizationManager &ltMethodInvocationResult></code>
//      * </pre>
//      *
//      * <h4>注意事项：</h4>
//      * 将上述两个方法按照自定义的方式进行实现后，还需要禁用默认的。
//      * <pre>
//      * &#064;Configuration
//      * &#064;EnableMethodSecurity(prePostEnabled = false)
//      * class MethodSecurityConfig {
//      *     &#064;Bean
//      *     &#064;Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//      *    Advisor preAuthorize(MyAuthorizationManager manager) {
//      * 		return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(manager);
//      *    }
//      *
//      *    &#064;Bean
//      *    &#064;Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//      *    Advisor postAuthorize(MyAuthorizationManager manager) {
//      * 		return AuthorizationManagerAfterMethodInterceptor.postAuthorize(manager);
//      *    }
//      * }
//      * </pre>
//      */
//     @Override
//     public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocationResult invocation) {
//         return new AuthorizationDecision(true);
//     }
// }