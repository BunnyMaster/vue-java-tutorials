package com.design.patterns.abstractfactory.product.table;

import java.math.BigDecimal;

/**
 * 艺术风格茶几
 *
 * @author bunny
 */
public class ArtDecoTeaTable implements TeaTable {

    private static final BigDecimal PRICE = new BigDecimal("899.99");
    private static final double WEIGHT = 15.5;
    private static final String COLOR = "金色和黑色";
    private static final String MATERIAL = "镀铬金属和玻璃";

    /**
     * 获取描述
     *
     * @return 描茶几述
     */
    @Override
    public String getDescription() {
        return "ArtDecoTeaTable";
    }

    /**
     * 获取价格
     *
     * @return 价格
     */
    @Override
    public BigDecimal getPrice() {
        return PRICE;
    }

    /**
     * 获取重量
     *
     * @return 重量
     */
    @Override
    public double getWeight() {
        return WEIGHT;
    }

    /**
     * 获取颜色
     *
     * @return 颜色
     */
    @Override
    public String getColor() {
        return COLOR;
    }

    /**
     * 获取材质
     *
     * @return 材质
     */
    @Override
    public String getMaterial() {
        return MATERIAL;
    }
}
