package cn.bunny.drools.demo;

import cn.bunny.drools.bean.demo.demo3.CreditCardApplication;
import cn.bunny.drools.config.DroolsConfiguration;
import org.kie.api.runtime.KieSession;

public class _03CreditCardApplicationTest {
    public static void main(String[] args) {
        // 测试1: 高额度批准
        CreditCardApplication app1 = CreditCardApplication.builder()
                .applicantId("001")
                .applicantName("张三")
                .creditScore(750)
                .annualIncome(60000)  // 修正为符合条件
                .existingDebt(5000)
                .build();
        app1 = processApplication(app1);
        System.out.println("结果1: " + app1.getDecision() + ", 额度: " + app1.getApprovedLimit());

        // 测试2: 中等额度批准
        CreditCardApplication app2 = CreditCardApplication.builder()
                .applicantId("002")
                .applicantName("李四")
                .creditScore(650)
                .annualIncome(45000)
                .existingDebt(10000)
                .build();
        app2 = processApplication(app2);
        System.out.println("结果2: " + app2.getDecision() + ", 额度: " + app2.getApprovedLimit());

        // 测试3: 低额度批准(高收入但低信用分)
        CreditCardApplication app3 = CreditCardApplication.builder()
                .applicantId("003")
                .applicantName("王五")
                .creditScore(550)
                .annualIncome(120000)
                .existingDebt(20000)
                .build();
        app3 = processApplication(app3);
        System.out.println("结果3: " + app3.getDecision() + ", 额度: " + app3.getApprovedLimit());

        // 测试4: 负债过高拒绝
        CreditCardApplication app4 = CreditCardApplication.builder()
                .applicantId("004")
                .applicantName("赵六")
                .creditScore(720)
                .annualIncome(50000)
                .existingDebt(30000)
                .build();
        app4 = processApplication(app4);
        System.out.println("结果4: " + app4.getDecision() + ", 额度: " + app4.getApprovedLimit());

        // 测试5: 默认拒绝
        CreditCardApplication app5 = CreditCardApplication.builder()
                .applicantId("005")
                .applicantName("钱七")
                .creditScore(580)
                .annualIncome(40000)
                .existingDebt(5000)
                .build();
        app5 = processApplication(app5);
        System.out.println("结果5: " + app5.getDecision() + ", 额度: " + app5.getApprovedLimit());
    }

    public static CreditCardApplication processApplication(CreditCardApplication application) {
        try (KieSession kieSession = DroolsConfiguration.createKieSession("demo/credit-card-approval.drl")) {
            kieSession.insert(application);
            kieSession.fireAllRules();
            return application;
        }
    }
}