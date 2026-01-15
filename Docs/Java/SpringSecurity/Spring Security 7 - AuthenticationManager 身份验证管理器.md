## `AuthenticationManager`

虽然身份验证管理器的实现方式可以多种多样，但最常见的实现方式是 `ProviderManager`。

### `ProviderManager`

`ProviderManager`的父类是`AuthenticationManager`，也是 `AuthenticationManager` 最常用的实现方式，实现了以下内容：

```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {
}
```

`ProviderManager` 是 `AuthenticationManager` 中最常用的实现方式。`ProviderManager` 会委托给一个包含多个 `AuthenticationProvider` 实例的列表，每个 `AuthenticationProvider` 都有机会表明认证成功、失败，或者表示无法做出并允许下少游的 `AuthenticationProvider` 做出决定。

如果配置的任何`“AuthenticationProvider”`实例都无法进行认证，认证就会失败，并抛出`“ProviderNotFoundException”`异常，这是一种特殊的`“AuthenticationException”`，它表明` ProviderManager `未被配置为支持传入的认证类型。

同样多个`ProviderManager`可能会共用一个`AuthenticationManager`

在默认情况下`ProviderManager`会尝试清除成功认证请求所返回的认证对象中任何敏感信息，这样可以避免诸如密码等信息在`HttpSession` 中保留的时间超过必要期限。所以`CredentialsContainer`在这里有着重要作用，它允许在不再需要时删除凭证信息，从而通过确保敏感数据不会被保留超过必要的时间来提高安全性。

### `AuthenticationProvider`

源码如下：

```java
public interface AuthenticationProvider {
	Authentication authenticate(Authentication authentication) throws AuthenticationException;
	boolean supports(Class<?> authentication);
}
```

### `AbstractAuthenticationProcessingFilter`