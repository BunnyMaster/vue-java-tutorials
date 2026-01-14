package com.design.patterns.abstractfactory.product.sofa;

import java.math.BigDecimal;

/**
 * 维多利亚风格沙发
 *
 * @author bunny
 */
public class VictorianSofa implements Sofa {

    private static final BigDecimal PRICE = new BigDecimal("2500.00");
    private static final double WEIGHT = 85.5;
    private static final String COLOR = "深红色";
    private static final String MATERIAL = "天鹅绒和橡木";

    /**
     * 获取沙发描述
     *
     * @return 沙发描述
     */
    @Override
    public String getDescription() {
        return "维多利亚风格沙发";
    }

    /**
     * 获取沙发价格
     *
     * @return 沙发价格
     */
    @Override
    public BigDecimal getPrice() {
        return PRICE;
    }

    /**
     * 获取沙发重量
     *
     * @return 沙发重量
     */
    @Override
    public double getWeight() {
        return WEIGHT;
    }

    /**
     * 获取沙发颜色
     *
     * @return 沙发颜色
     */
    @Override
    public String getColor() {
        return COLOR;
    }

    /**
     * 获取沙发材质
     *
     * @return 沙发材质
     */
    @Override
    public String getMaterial() {
        return MATERIAL;
    }
}
