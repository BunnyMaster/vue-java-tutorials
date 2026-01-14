package cn.bunny.service.feign.fallback;

import cn.bunny.model.product.bean.Product;
import cn.bunny.service.feign.ProductFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductFeignClientFallback implements ProductFeignClient {
    @Override
    public Product getProduct(Long productId) {
        System.out.println("ProductFeignClientFallback 兜底回调...");

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("1000"));
        product.setProductName("兜底数据");
        product.setNum(99);
        return product;
    }
}
