package cn.bunny.drools.exercise;

import cn.bunny.drools.bean.exercise.ComparisonOperation;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.List;

public class Exercise01ComparisonOperationTest {

    /* contains */
    @Test
    void test1() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("张三名称").build();
        zxc.setOperatorList(List.of(zxc.getField()));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules();
        session.dispose();

        session.close();
    }

    /* not contains */
    @Test
    void test2() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("qwe").build();
        zxc.setOperatorList(List.of(zxc.getField()));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules();
        session.dispose();

        session.close();
    }

    /* memberOf */
    @Test
    void test3() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");

        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("qwe").build();
        zxc.setOperatorList(List.of(zxc.getField(), "sss"));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules();
        session.dispose();

        session.close();
    }

    /* matches & not matches */
    @Test
    void test4() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("qwe").build();
        zxc.setOperatorList(List.of(zxc.getField(), "sss"));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules();
        session.dispose();

        session.close();
    }

    /* 指定规则后缀进行匹配 */
    @Test
    void test5() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("qwe").build();
        zxc.setOperatorList(List.of(zxc.getField(), "sss"));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules(new RuleNameEndsWithAgendaFilter("exercise-04-contains"));
        session.dispose();

        session.close();
    }

    /* 以什么前缀匹配 */
    @Test
    void test6() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("qwe").build();
        zxc.setOperatorList(List.of(zxc.getField(), "sss"));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules(new RuleNameStartsWithAgendaFilter("exercise-0"));
        session.dispose();
        session.close();
    }

    /* 以正则匹配regexp */
    @Test
    void test7() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("Exercise");
        KieSession session = kieBase.newKieSession();

        ComparisonOperation zxc = ComparisonOperation.builder().field("qwe").build();
        zxc.setOperatorList(List.of(zxc.getField(), "sss"));

        // 插入匹配
        session.insert(zxc);
        session.fireAllRules(new RuleNameMatchesAgendaFilter("exercise-0.*?"));
        session.dispose();

        session.close();
    }
}
