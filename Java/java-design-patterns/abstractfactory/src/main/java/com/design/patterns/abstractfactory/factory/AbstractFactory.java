package com.design.patterns.abstractfactory.factory;

import com.design.patterns.abstractfactory.product.chair.Chair;
import com.design.patterns.abstractfactory.product.sofa.Sofa;
import com.design.patterns.abstractfactory.product.table.TeaTable;

import java.math.BigDecimal;

/**
 * 定义了创建家具系列产品的抽象接口,包括椅子、沙发和茶几的创建方法
 *
 * @author bunny
 */
public abstract class AbstractFactory {

    /**
     * 创建家具系列产品
     * 这个只是一个示例，如果有需要可以做一个枚举类将不同的工厂放到对应枚举中
     *
     * @return 创建家具系列产品
     */
    public String createProducts() {
        Chair chair = createChair();
        double chairWeight = chair.getWeight();
        BigDecimal chairPrice = chair.getPrice();
        String chairDescription = chair.getDescription();
        String chairMaterial = chair.getMaterial();
        String color = chair.getColor();

        Sofa sofa = createSofa();
        double sofaWeight = sofa.getWeight();
        BigDecimal sofaPrice = sofa.getPrice();
        String sofaDescription = sofa.getDescription();
        String sofaMaterial = sofa.getMaterial();
        String sofaColor = sofa.getColor();

        TeaTable teaTable = createTeaTable();
        double teaTableWeight = teaTable.getWeight();
        BigDecimal teaTablePrice = teaTable.getPrice();
        String teaTableDescription = teaTable.getDescription();
        String teaTableMaterial = teaTable.getMaterial();
        String teaTableColor = teaTable.getColor();

        return "椅子：" + chairDescription + "，价格：" + chairPrice + "，重量：" + chairWeight + "，颜色：" + color + "，材质：" + chairMaterial + "\n" +
                "沙发：" + sofaDescription + "，价格：" + sofaPrice + "，重量：" + sofaWeight + "，颜色：" + sofaColor + "，材质：" + sofaMaterial + "\n" +
                "茶几：" + teaTableDescription + "，价格：" + teaTablePrice + "，重量：" + teaTableWeight + "，颜色：" + teaTableColor + "，材质：" + teaTableMaterial + "\n";
    }

    /**
     * 创建椅子产品
     *
     * @return 返回具体的椅子对象实例
     */
    public abstract Chair createChair();

    /**
     * 创建沙发产品
     *
     * @return 返回具体的沙发对象实例
     */
    public abstract Sofa createSofa();

    /**
     * 创建茶几产品
     *
     * @return 返回具体的茶几对象实例
     */
    public abstract TeaTable createTeaTable();
}
