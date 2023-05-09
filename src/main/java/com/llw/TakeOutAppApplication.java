package com.llw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement //开启SpringBoot数据库事务功能
@EnableCaching //开启SpringBoot缓存注解功能
public class TakeOutAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeOutAppApplication.class, args);
    }

}
