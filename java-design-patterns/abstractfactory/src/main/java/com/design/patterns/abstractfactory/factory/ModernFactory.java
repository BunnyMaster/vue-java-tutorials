package com.design.patterns.abstractfactory.factory;

import com.design.patterns.abstractfactory.product.chair.Chair;
import com.design.patterns.abstractfactory.product.chair.ModernChair;
import com.design.patterns.abstractfactory.product.sofa.ModernSofa;
import com.design.patterns.abstractfactory.product.sofa.Sofa;
import com.design.patterns.abstractfactory.product.table.ModernTeaTable;
import com.design.patterns.abstractfactory.product.table.TeaTable;

/**
 * 现代工厂类
 *
 * @author bunny
 */
public class ModernFactory extends AbstractFactory {
    /**
     * 创建椅子产品
     *
     * @return 返回具体的椅子对象实例
     */
    @Override
    public Chair createChair() {
        return new ModernChair();
    }

    /**
     * 创建沙发产品
     *
     * @return 返回具体的沙发对象实例
     */
    @Override
    public Sofa createSofa() {
        return new ModernSofa();
    }

    /**
     * 创建茶几产品
     *
     * @return 返回具体的茶几对象实例
     */
    @Override
    public TeaTable createTeaTable() {
        return new ModernTeaTable();
    }
}
