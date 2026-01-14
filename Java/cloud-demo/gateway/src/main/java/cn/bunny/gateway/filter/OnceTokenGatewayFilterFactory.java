package cn.bunny.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        // 每次相应之前添加一次性令牌

        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders headers = response.getHeaders();

                    String name = config.getName();
                    String value = config.getValue();

                    if ("uuid".equalsIgnoreCase(value)) {
                        value = UUID.randomUUID().toString();
                    }

                    if ("jwt".equalsIgnoreCase(value)) {
                        value = "JWT的token";
                    }

                    headers.add(name, value);
                }));
    }
}
