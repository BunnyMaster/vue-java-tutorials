package com.design.patterns.abstractfactory.product.chair;

import java.math.BigDecimal;

/**
 * 椅子
 *
 * @author bunny
 */
public interface Chair {

    /**
     * 获取椅子描述
     *
     * @return 椅子描述
     */
    String getDescription();

    /**
     * 获取椅子价格
     *
     * @return 椅子价格
     */
    BigDecimal getPrice();

    /**
     * 获取椅子重量
     *
     * @return 椅子重量
     */
    double getWeight();

    /**
     * 获取椅子颜色
     *
     * @return 椅子颜色
     */
    String getColor();

    /**
     * 获取椅子材质
     *
     * @return 椅子材质
     */
    String getMaterial();
}
