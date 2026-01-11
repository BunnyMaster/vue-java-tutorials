package com.design.patterns.abstractfactory.product.chair;

import java.math.BigDecimal;


/**
 * 艺术风格椅子
 *
 * @author bunny
 */
public class ArtDecoChair implements Chair {
    private static final BigDecimal PRICE = new BigDecimal("899.99");
    private static final double WEIGHT = 12.5;
    private static final String COLOR = "金色和黑色";
    private static final String MATERIAL = "高级木材和金属";

    /**
     * 获取椅子描述
     *
     * @return 椅子描述
     */
    @Override
    public String getDescription() {
        return "艺术风格椅子 - 具有独特的几何图案和奢华材料的经典设计";
    }

    /**
     * 获取椅子价格
     *
     * @return 椅子价格
     */
    @Override
    public BigDecimal getPrice() {
        return PRICE;
    }

    /**
     * 获取椅子重量
     *
     * @return 椅子重量
     */
    @Override
    public double getWeight() {
        return WEIGHT;
    }

    /**
     * 获取椅子颜色
     *
     * @return 椅子颜色
     */
    @Override
    public String getColor() {
        return COLOR;
    }

    /**
     * 获取椅子材质
     *
     * @return 椅子材质
     */
    @Override
    public String getMaterial() {
        return MATERIAL;
    }
}
