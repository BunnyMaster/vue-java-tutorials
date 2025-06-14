package cn.bunny.service.feign;

import cn.bunny.model.product.bean.Product;
import cn.bunny.service.feign.fallback.ProductFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign 客户端
@FeignClient(value = "gateway", path = "/api/product", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {

    // 标注在 Controller 上是接受请求
    // 标注在 FeignClient 时发送请求
    @GetMapping("{id}")
    Product getProduct(@PathVariable("id") Long productId);

}
