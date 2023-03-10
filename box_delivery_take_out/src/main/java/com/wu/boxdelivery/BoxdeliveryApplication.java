package com.wu.boxdelivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement//开启事务注解
public class BoxdeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoxdeliveryApplication.class,args);
        log.info("项目启动成功!!");//输出info的日志
    }
}
