package com.design.patterns.abstractfactory;

import com.design.patterns.abstractfactory.factory.AbstractFactory;
import com.design.patterns.abstractfactory.factory.ArtDecoFactory;

/**
 * 抽象工厂模式
 * 如果使用 （{@link AbstractFactory}） 实现的类进行创建
 * 主这里所有的 接口实现可以换成其他的，为了方便所有的 Product 里面内容都是一样的
 *
 * @author bunny
 */
public class App {
    public static void main(String[] args) {
        // 创建Art Deco风格的具体工厂实例
        AbstractFactory factory = new ArtDecoFactory();

        // 通过工厂创建产品族并获取产品信息
        String products = factory.createProducts();

        // 输出创建的产品信息
        System.out.println(products);
    }
}
