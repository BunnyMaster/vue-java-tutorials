package com.design.patterns.abstractfactory.product.chair;


import java.math.BigDecimal;


/**
 * 现代椅子
 *
 * @author bunny
 */
public class ModernChair implements Chair {

    private static final BigDecimal PRICE = new BigDecimal("129.99");
    private static final double WEIGHT = 8.5;
    private static final String COLOR = "黑色";
    private static final String MATERIAL = "金属和皮革";

    /**
     * 获取椅子描述
     *
     * @return 椅子描述
     */
    @Override
    public String getDescription() {
        return "现代椅子";
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
