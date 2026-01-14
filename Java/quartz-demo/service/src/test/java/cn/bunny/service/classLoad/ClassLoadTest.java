package cn.bunny.service.classLoad;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class ClassLoadTest {

    // 动态加载类
    @Test
    void testClassLoad() throws ClassNotFoundException {
        Thread thread = Thread.currentThread();
        ClassLoader contextClassLoader = thread.getContextClassLoader();
        Class<?> aClass = contextClassLoader.loadClass("cn.bunny.service.job.MyJob");
        for (Field field : aClass.getFields()) {
            System.out.println(field);
        }
        String name = aClass.getName();
        System.out.println(name);
    }
}
