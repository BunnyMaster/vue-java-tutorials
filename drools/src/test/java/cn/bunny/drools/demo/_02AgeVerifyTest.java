package cn.bunny.drools.demo;

import cn.bunny.drools.bean.demo.demo2.AccessRequest;
import cn.bunny.drools.bean.demo.demo2.Content;
import cn.bunny.drools.bean.demo.demo2.ContentType;
import cn.bunny.drools.bean.demo.demo2.User;
import cn.bunny.drools.config.DroolsConfiguration;
import org.kie.api.runtime.KieSession;

public class _02AgeVerifyTest {
    private static AccessRequest verifyAccess(User adult, Content rRatedMovie) {
        // 测试1: 成人可以访问所有内容
        AccessRequest request = AccessRequest.builder().user(adult).content(rRatedMovie).build();
        // 默认允许
        request.setGranted(true);

        try (KieSession kieSession = DroolsConfiguration.createKieSession("demo/age-verify.drl")) {

            kieSession.insert(request);
            kieSession.insert(adult);
            kieSession.insert(rRatedMovie);

            kieSession.fireAllRules();
            kieSession.dispose();
        }

        return request;
    }

    public static void main(String[] args) {
        User adult = new User("成人用户", 25);
        User teen = new User("青少年", 17);
        User youngAdult = new User("年轻成人", 20);

        Content rRatedMovie = new Content("R级电影", ContentType.R_RATED);
        Content alcohol = new Content("啤酒", ContentType.ALCOHOL);
        Content general = new Content("普通内容", ContentType.GENERAL);

        // 测试1: 成人可以访问所有内容
        AccessRequest request1 = verifyAccess(adult, alcohol);
        System.out.println(request1.isGranted());

        AccessRequest request2 = verifyAccess(adult, rRatedMovie);
        System.out.println(request2.isGranted());

        // 测试2: 青少年不能访问R级内容
        AccessRequest request3 = verifyAccess(teen, rRatedMovie);
        System.out.println(request3.isGranted());
        System.out.println(request3.getDenialReason());

        // 测试3: 20岁用户不能购买酒精
        AccessRequest request4 = verifyAccess(youngAdult, alcohol);
        System.out.println(request4.isGranted());
        System.out.println(request4.getDenialReason());

        // 测试4: 所有用户都可以访问普通内容
        AccessRequest request5 = verifyAccess(teen, general);
        System.out.println(request5.isGranted());
        System.out.println(request5.getDenialReason());
    }
}
