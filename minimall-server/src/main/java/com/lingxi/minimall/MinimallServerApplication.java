package com.lingxi.minimall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** 启动内嵌 Tomcat，并让 Spring 扫描当前包下的 Controller、Service 和 Mapper。 */
@SpringBootApplication
@EnableScheduling
public class MinimallServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimallServerApplication.class, args);
    }
}
