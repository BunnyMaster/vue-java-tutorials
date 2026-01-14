package com.design.patterns.abstractfactory.product.table;

import java.math.BigDecimal;

/**
 * 茶几
 *
 * @author bunny
 */
public interface TeaTable {

    /**
     * 获取描述
     *
     * @return 描茶几述
     */
    String getDescription();

    /**
     * 获取价格
     *
     * @return 价格
     */
    BigDecimal getPrice();

    /**
     * 获取重量
     *
     * @return 重量
     */
    double getWeight();

    /**
     * 获取颜色
     *
     * @return 颜色
     */
    String getColor();

    /**
     * 获取材质
     *
     * @return 材质
     */
    String getMaterial();
}
