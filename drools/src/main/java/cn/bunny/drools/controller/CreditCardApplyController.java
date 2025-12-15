package cn.bunny.drools.controller;

import cn.bunny.drools.bean.exercise.CreditCardApply;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "申请信用卡")
@RequestMapping("/api/credit-card")
public class CreditCardApplyController {

    @SneakyThrows
    @PostMapping("")
    @Operation(summary = "申请信用卡")
    public CreditCardApply creditCardApply(@RequestBody CreditCardApply creditCardApply) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        // KieBase kieBase = kieContainer.getKieBase("CreditCardApply");
        KieSession session = kieContainer.newKieSession("credit_card_apply_card_1");

        session.insert(creditCardApply);
        session.fireAllRules();
        session.dispose();
        session.close();
        return creditCardApply;
    }
}
