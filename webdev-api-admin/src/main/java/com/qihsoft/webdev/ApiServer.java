package com.qihsoft.webdev;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Qihsoft on 2017/11/3
 */
@SpringBootApplication
@MapperScan("com.qihsoft.webdev.core.mapper")
public class ApiServer {
    public static void main(String[] args) {
        SpringApplication.run(ApiServer.class, args);
    }
}
