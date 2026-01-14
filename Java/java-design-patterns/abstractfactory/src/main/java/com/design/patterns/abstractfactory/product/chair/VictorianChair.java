package com.design.patterns.abstractfactory.product.chair;

import java.math.BigDecimal;


/**
 * 维多利亚椅子
 *
 * @author bunny
 */
public class VictorianChair implements Chair {

    private static final BigDecimal VICTORIAN_CHAIR_PRICE = new BigDecimal("299.99");
    private static final double VICTORIAN_CHAIR_WEIGHT = 15.5;
    private static final String VICTORIAN_CHAIR_COLOR = "深棕色";
    private static final String VICTORIAN_CHAIR_MATERIAL = "桃花心木和天鹅绒";

    /**
     * 获取椅子描述
     *
     * @return 椅子描述
     */
    @Override
    public String getDescription() {
        return "维多利亚风格手工雕刻椅子，经典复古设计";
    }

    /**
     * 获取椅子价格
     *
     * @return 椅子价格
     */
    @Override
    public BigDecimal getPrice() {
        return VICTORIAN_CHAIR_PRICE;
    }

    /**
     * 获取椅子重量
     *
     * @return 椅子重量
     */
    @Override
    public double getWeight() {
        return VICTORIAN_CHAIR_WEIGHT;
    }

    /**
     * 获取椅子颜色
     *
     * @return 椅子颜色
     */
    @Override
    public String getColor() {
        return VICTORIAN_CHAIR_COLOR;
    }

    /**
     * 获取椅子材质
     *
     * @return 椅子材质
     */
    @Override
    public String getMaterial() {
        return VICTORIAN_CHAIR_MATERIAL;
    }
}
