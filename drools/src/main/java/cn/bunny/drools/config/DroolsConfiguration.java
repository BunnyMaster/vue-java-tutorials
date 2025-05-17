package cn.bunny.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class DroolsConfiguration {

    /**
     * 读取单个文件内容
     *
     * @param filename 规则文件名.后缀名
     * @return KieContainer
     */
    public static KieSession createKieSession(String filename) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write(ResourceFactory.newClassPathResource("rules/" + filename));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        KieContainer container = kieServices.newKieContainer(kieModule.getReleaseId());

        return container.newKieSession();
    }
}
