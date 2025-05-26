package cn.bunny.service.service.impl;

import cn.bunny.model.order.bean.Order;
import cn.bunny.model.product.bean.Product;
import cn.bunny.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    /**
     * 创建订单信息
     *
     * @param productId 订单信息
     * @param userId    用户id
     * @return 订单内容
     */
    @Override
    public Order createOrder(Long productId, Long userId) {
        Product product = getProductFromRemote(productId);

        Order order = new Order();
        order.setId(1L);
        // 查询总金额
        BigDecimal amount = product.getPrice().multiply(new BigDecimal(product.getNum()));
        order.setTotalAmount(amount);
        order.setNickName("在资质");
        order.setAddress("地址地址。。。");

        // TODO 远程查询
        order.setProductList(List.of(product));
        return order;
    }

    private Product getProductFromRemote(Long productId) {
        // 获取商品服务所有及其的 IP+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance instance = instances.get(0);

        // 远程URL
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/api/product/" + productId;

        // 2. 远程发送请求
        log.info("远程调用：{}", url);
        return restTemplate.getForObject(url, Product.class);
    }
}
