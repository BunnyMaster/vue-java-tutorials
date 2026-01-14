package cn.bunny.service.service;

import cn.bunny.model.product.bean.Product;

public interface ProductService {
    /**
     * 根据id获取商品
     *
     * @param productId 商品id
     * @return 商品内容
     */
    Product getProductById(Long productId);
}
