## `SecurityContextHolder`

> [!NOTE]
>
> 文档下载地址：https://github.com/spring-projects/spring-security/blob/7.0.2/docs/modules/ROOT/pages/index.adoc
>
> 在页面中有：**Spring Security’s documentation can be [downloaded](https://docs.spring.io/spring-security/reference/spring-security-docs.zip) as a zip file.**

如果文档下载完成需要运行，如Ubuntu、Kubuntu等可以直接使用命令：`python3 -m http.server 6060`。其中`python3`是系统自带的，包括`http.server`也是系统自带的。

![结构包含图](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/securitycontextholder.png)

- `SecurityContextHolder`是 Spring 安全框架用于存储已认证用户相关信息的地方。
  - Spring 安全框架并不关心如何填充“安全上下文管理器”。只要其中包含有值，就会将其用作当前已认证的用户。
  - 表明用户已通过身份验证的最简单方法是直接设置 `SecurityContextHolder`：

一个简单的示例：

```java
SecurityContext context = SecurityContextHolder.createEmptyContext();
Authentication authentication = new TestingAuthenticationToken("username", "password", "ROLE_USER");
context.setAuthentication(authentication);

SecurityContextHolder.setContext(context);
```

默认是用`ThreadLocal`进行存储的，也就是说当前用户是在同一线程下才可以被访问到。`FilterChainProxy`负责清除上下文的信息。

在一些应用程序中是不支持`ThreadLocal`的，比如：swing。如果要更改可以参考相应JavaDOC
,这是从源码中看到有这几种：

```java
public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
public static final String MODE_GLOBAL = "MODE_GLOBAL";
private static final String MODE_PRE_INITIALIZED = "MODE_PRE_INITIALIZED";
```

### `SecurityContext`

#### Authentication

向身份验证管理器提供一个输入，以获取用户用于身份验证所提供的凭证。在这种使用场景中，isAuthenticated() 方法将返回 false 。