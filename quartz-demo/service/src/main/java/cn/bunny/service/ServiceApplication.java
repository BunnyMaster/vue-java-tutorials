package cn.bunny.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = {"cn.bunny"})
@MapperScan("cn.bunny.service.mapper")
@EnableScheduling// 定时任务
@EnableCaching// 开启缓存注解
@EnableTransactionManagement// 开启事务注解
@SpringBootApplication
@Slf4j
public class ServiceApplication {
    public static void main(String[] args) {
        log.info("ServiceApplication启动...");
        SpringApplication.run(ServiceApplication.class, args);
    }
}
