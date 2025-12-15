package cn.bunny.drools.controller;

import cn.bunny.drools.bean.exercise.CalculateTax;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "计算税额")
@RequestMapping("/api/calculate-tax")
public class CalculateTaxController {

    // @Autowired
    // private DroolsConfiguration droolsConfiguration;

    @SneakyThrows
    @GetMapping("")
    @Operation(summary = "计算税后工资")
    public CalculateTax getCalculateTax(double amount) {
        // KieBase kieBase = droolsConfiguration.kieBase();
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieBase kieBase = container.getKieBase("ExerciseTax");
        KieSession session = kieBase.newKieSession();

        CalculateTax calculateTax = CalculateTax.builder().wage(amount).build();
        session.insert(calculateTax);
        session.fireAllRules();
        session.dispose();
        session.close();
        return calculateTax;
    }
}
