package cn.bunny.drools.demo;

import lombok.SneakyThrows;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DrlResourceTypeTest {
    @SneakyThrows
    public static void main(String[] args) {
        KieHelper kieHelper = new KieHelper();
        KieHelper addContent = kieHelper.addContent("", ResourceType.DRL);
        KieSession kieSession = kieHelper.build().newKieSession();

        // 指定决策表xls文件的磁盘路径
        String realPath = "C:\\testRule.xls";
        File file = new File(realPath);
        InputStream is = new FileInputStream(file);
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String drl = compiler.compile(is, InputType.XLS);

        kieSession.dispose();
        kieSession.close();
    }
}
