package cn.bunny.drools.demo;

import cn.bunny.drools.bean.demo.BookDisCount;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class _00BookDisCountTest {
    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.newKieClasspathContainer();
        KieSession kieSession = container.newKieSession();

        BookDisCount order = new BookDisCount();
        order.setOriginalPrice(199.0);

        // 插入 Fact
        kieSession.insert(order);

        // 激活规则
        kieSession.fireAllRules();

        // 关闭会话
        kieSession.dispose();

        System.out.println(order);
        kieSession.close();
    }
}
