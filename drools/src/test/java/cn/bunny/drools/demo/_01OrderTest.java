package cn.bunny.drools.demo;

import cn.bunny.drools.bean.demo.demo1.Order;
import cn.bunny.drools.config.DroolsConfiguration;
import org.kie.api.runtime.KieSession;

import java.math.BigDecimal;

class _01OrderTest {
    public static void main(String[] args) {
        try (KieSession kieSession = DroolsConfiguration.createKieSession("demo/order.drl")) {

            Order order = new Order();
            order.setAmount(new BigDecimal(99));
            order.setAmount(new BigDecimal(101));
            // order.setVip(true);

            // 插入order
            kieSession.insert(order);
            kieSession.fireAllRules();
            kieSession.dispose();

            System.out.println(order);
        }
    }
}