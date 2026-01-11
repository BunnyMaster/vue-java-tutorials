package com.design.patterns.abstractfactory;

import com.design.patterns.abstractfactory.factory.AbstractFactory;
import com.design.patterns.abstractfactory.factory.ArtDecoFactory;
import com.design.patterns.abstractfactory.factory.ModernFactory;
import com.design.patterns.abstractfactory.factory.VictorianFactory;
import lombok.Getter;

/**
 * 抽象工厂枚举类
 *
 * @author bunny
 */
@Getter
public enum FactoryEnum {
    /**
     * 艺术风格工厂类
     */
    ArtDeco,
    /**
     * 现代风格工厂类
     */
    Modern,
    /**
     * 维多利亚风格工厂类
     */
    Victorian,
    ;

    public static AbstractFactory getFactory(FactoryEnum factory) {
        return switch (factory) {
            case ArtDeco -> new ArtDecoFactory();
            case Modern -> new ModernFactory();
            case Victorian -> new VictorianFactory();
            default -> throw new IllegalArgumentException("Invalid Factory");
        };
    }
}
