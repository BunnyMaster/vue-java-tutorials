![结构包含图](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/securitycontextholder.png)

## `SecurityContextHolder`

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

## `SecurityContext`

安全上下文是从`SecurityContext`中获取的。该安全上下文包含一个认证对象，即`Authentication`。

### Authentication

**示例代码：**

```java
SecurityContext context = SecurityContextHolder.createEmptyContext();

TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken("user", "password", AuthorityUtils.createAuthorityList("ROLE_USER"));
```

**`Authentication`主要有两个用途：**

1. 向身份验证管理器提供一个输入，以获取用户用于身份验证所提供的凭证。在这种使用场景中，`isAuthenticated() `方法将返回 false 。
2. 第二种用法表示已认证的用户，可以使用`SecurityContext`从`Authentication`中获取当前认证的信息。

从 `Authentication` 中获取认证信息包含：

**`principal`：标识用户身份**

在如下中，`principal`表示`"user"

**credentials：通常是一个密码**

在`TestingAuthenticationToken`类中用`"password"`表示。在很多情况下，该密码在用户通过身份验证后会被清除，以确保其不会泄露。

**authorities：指用户权限**

在上面示例中指的是`AuthorityUtils.createAuthorityList("ROLE_USER")`一般表示解馋和权限范围。

## `GrantedAuthority`

一般用于用户获得的高级权限。可以通过`Authentication.getAuthorities() `方法获取`GrantedAuthority`实例/

## `AuthenticationManager`

TODO：未知

### `ProviderManager`

的父类是`AuthenticationManager`，实现了以下内容：

```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {}
```

在默认情况下`ProviderManager`会尝试清除成功认证请求所返回的认证对象中任何敏感信息，这样可以避免诸如密码等信息在 `HttpSession` 中保留的时间超过必要期限。所以`CredentialsContainer`在这里有着重要作用，它允许在不再需要时删除凭证信息，从而通过确保敏感数据不会被保留超过必要的时间来提高安全性。

同样多个`ProviderManager`可能会共用一个`AuthenticationManager`

**如何验证？**

`ProviderManager` 会委托给一个包含多个`“AuthenticationProvider”`实例的列表。`AuthenticationProvider`会表明是否认证成功或者失败。

如果上述都无法做出决策，会允许交给下游`AuthenticationProvider`对其验证。

**错误提示：**

如果配置的任何`“AuthenticationProvider”`实例都无法进行认证，认证就会失败，并抛出`“ProviderNotFoundException”`异常，这是一种特殊的`“AuthenticationException”`，它表明` ProviderManager `未被配置为支持传入的认证类型。

### `AuthenticationProvider`

### `AbstractAuthenticationProcessingFilter`