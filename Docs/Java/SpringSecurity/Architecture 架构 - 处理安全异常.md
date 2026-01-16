## 处理安全异常

`ExceptionTranslationFilter` 能够将`AccessDeniedException`和`AuthenticationException`这两种异常转换为 HTTP 响应。异常转换过滤器被插入到过滤器链代理中，并作为安全过滤器之一而存在。
以下图片展示了 `ExceptionTranslationFilter` 与其他组件之间的关系：

![Handling Security Exceptions](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/exceptiontranslationfilter.png)

在上图中解释如下：

1. `ExceptionTranslationFilter` 会调用 `FilterChain.doFilter(request, response)` 其余部分。
2. 如果当前认证出现了问题，会启动身份验证流程
   - 安全上下文管理器已被清空
   - 会将 `HttpServletRequest` 保存下来，以便在身份验证成功后能够使用它来重现原始请求。
   - 认证入口点用于向客户端请求凭证。例如，它可能会重定向到登录页面或者发送一个“WWW-Authenticate”标头。
3. 否则，如果抛出的是“访问被拒绝异常”，那么就表示“访问被拒绝”。此时会调用“访问被拒绝处理程序”来处理访问被拒绝的情况。

如果应用程序没有抛出“访问被拒绝异常”或“认证异常”，那么“异常转换过滤器”就不会执行任何操作。

