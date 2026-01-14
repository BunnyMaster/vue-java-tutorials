package com.design.patterns.abstractfactory.product.sofa;

import java.math.BigDecimal;

/**
 * 现代沙发
 *
 * @author bunny
 */
public class ModernSofa implements Sofa {
    private static final BigDecimal PRICE = new BigDecimal("1299.99");
    private static final double WEIGHT = 45.5;
    private static final String COLOR = "黑色";
    private static final String MATERIAL = "皮革";

    /**
     * 获取沙发描述
     *
     * @return 沙发描述
     */
    @Override
    public String getDescription() {
        return "现代沙发";
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
