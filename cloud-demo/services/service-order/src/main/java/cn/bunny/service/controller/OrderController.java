package cn.bunny.service.controller;

import cn.bunny.model.order.bean.Order;
import cn.bunny.service.config.OrderProperties;
import cn.bunny.service.service.OrderService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // @Value("${order.timeout}")
    // private String timeout;
    //
    // @Value("${order.auto-confirm}")
    // private String autoConfirm;

    private final OrderProperties orderProperties;

    @Operation(summary = "创建订单")
    @SentinelResource(value = "order", blockHandler = "createBlockHandler")
    @GetMapping("create")
    public Order createOrder(Long userId, Long productId) {
        return orderService.createOrder(productId, userId);
    }

    public Order createBlockHandler(Long userId, Long productId, BlockException exception) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAddress("xxx");
        order.setNickName(exception.getMessage());
        order.setProductList(List.of());
        return order;
    }

    @Operation(summary = "读取配置")
    @GetMapping("config")
    public String config(HttpServletRequest request) {
        String timeout = orderProperties.getTimeout();
        String autoConfirm = orderProperties.getAutoConfirm();
        String dbUrl = orderProperties.getDbUrl();

        // 携带的请求头内容
        String header = request.getHeader("X-Request-red");
        log.info("Received headers: {}", header);

        return "timeout：" + timeout + "\nautoConfirm：" + autoConfirm + "\norder.db-url" + dbUrl;
    }
}
