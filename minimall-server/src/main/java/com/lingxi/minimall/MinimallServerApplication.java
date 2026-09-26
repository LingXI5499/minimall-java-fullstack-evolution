package com.lingxi.minimall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 启动内嵌 Tomcat，并让 Spring 扫描当前包下的 Controller、Service 和 Mapper。 */
@SpringBootApplication
public class MinimallServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimallServerApplication.class, args);
    }
}
