package cn.bunny.drools.tutorials;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;

public class _01InitDroolsTest {
    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();

        ClassLoader classLoader = container.getClassLoader();
        ReleaseId releaseId = container.getReleaseId();


        KieBase kieBase = container.getKieBase();
        // KieBase kieBase1 = container.newKieBase();
    }
}
