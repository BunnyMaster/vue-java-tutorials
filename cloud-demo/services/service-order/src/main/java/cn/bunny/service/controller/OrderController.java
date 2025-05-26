package cn.bunny.service.controller;

import cn.bunny.model.order.bean.Order;
import cn.bunny.service.config.OrderProperties;
import cn.bunny.service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("create")
    public Order createOrder(Long userId, Long productId) {
        return orderService.createOrder(productId, userId);
    }

    @Operation(summary = "读取配置")
    @GetMapping("config")
    public String config() {
        String timeout = orderProperties.getTimeout();
        String autoConfirm = orderProperties.getAutoConfirm();
        return "timeout：" + timeout + "\nautoConfirm：" + autoConfirm;
    }
}
