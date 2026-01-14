package cn.bunny.drools.tutorials;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

@Slf4j
public class _02GetKModuleXmlTest {
    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();

        // 验证所有可用KieBase
        log.info("Available KieBases: {}", container.getKieBaseNames());

        KieBase kieBase = container.getKieBase("KBase1");
        log.info("kieBase--->: {}", kieBase);

        KieSession kieSession = container.newKieSession("KSession2_1");
        log.info("kieSession--->: {}", kieSession);

        StatelessKieSession kSession2_2 = container.newStatelessKieSession("KSession2_2");
        log.info("kSession2_2--->: {}", kSession2_2);
    }
}
