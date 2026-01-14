package cn.bunny.service.service.impl;

import cn.bunny.model.product.bean.Product;
import cn.bunny.service.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    /**
     * 根据id获取商品
     *
     * @param productId 商品id
     * @return 商品内容
     */
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("10"));
        product.setProductName("xxx");
        product.setNum(2);
        return product;
    }
}
