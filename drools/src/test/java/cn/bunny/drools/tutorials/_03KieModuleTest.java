package cn.bunny.drools.tutorials;

import lombok.SneakyThrows;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.conf.ClockTypeOption;

public class _03KieModuleTest {
    /**
     * 动态化规则管理
     * 传统限制：kmodule.xml声明式配置需预定义规则路径，修改需重新部署
     * 编程优势：通过KieFileSystem/KieModuleModel可实现：
     * 运行时动态加载规则（如从数据库/网络获取DRL文件）
     * 热更新规则而不重启服务（结合KieScanner）
     */
    @SneakyThrows
    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();

        KieBaseModel kBase1 = kieModuleModel.newKieBaseModel("kBase1")
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.STREAM);

        kBase1.newKieSessionModel("KSession1")
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                .setClockType(ClockTypeOption.get("realtime"));

        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.writeKModuleXML(kieModuleModel.toXML());


        System.out.println(kieModuleModel.toXML());

        Thread.sleep(10000000);
    }
}
