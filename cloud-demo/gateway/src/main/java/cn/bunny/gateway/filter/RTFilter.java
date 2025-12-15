package cn.bunny.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class RTFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        long start = System.currentTimeMillis();
        log.info("请求【{}】开始时间：{}", uri, start);

        // 处理逻辑
        // return chain.filter(exchange)
        //         // 因为是异步的，不能写在下main，需要处理后续逻辑写在 doFinally
        //         .doFinally(result -> {
        //             long end = System.currentTimeMillis();
        //             log.error("请求【{}】结束 ，时间：{}，耗时:{}", uri, end, end - start);
        //         });
        return chain.filter(exchange)
                .doOnError(e -> log.error("请求失败", e))
                .doFinally(result -> {
                    long end = System.currentTimeMillis();
                    log.info("请求【{}】结束，状态：{}，耗时：{}ms",
                            uri, result, end - start);
                });
    }

    @Override
    public int getOrder() {
        return 0; // 执行顺序
    }
}