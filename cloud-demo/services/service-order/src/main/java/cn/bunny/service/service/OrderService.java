package cn.bunny.service.service;

import cn.bunny.model.order.bean.Order;

public interface OrderService {

    /**
     * 创建订单信息
     *
     * @param productId 订单信息
     * @param userId    用户id
     * @return 订单内容
     */
    Order createOrder(Long productId, Long userId);
}
