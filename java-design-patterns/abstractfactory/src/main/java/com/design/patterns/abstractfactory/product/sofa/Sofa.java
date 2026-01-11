package com.design.patterns.abstractfactory.product.sofa;

import java.math.BigDecimal;

/**
 * 沙发
 *
 * @author bunny
 */
public interface Sofa {

    /**
     * 获取沙发描述
     *
     * @return 沙发描述
     */
    String getDescription();

    /**
     * 获取沙发价格
     *
     * @return 沙发价格
     */
    BigDecimal getPrice();

    /**
     * 获取沙发重量
     *
     * @return 沙发重量
     */
    double getWeight();

    /**
     * 获取沙发颜色
     *
     * @return 沙发颜色
     */
    String getColor();

    /**
     * 获取沙发材质
     *
     * @return 沙发材质
     */
    String getMaterial();
}
