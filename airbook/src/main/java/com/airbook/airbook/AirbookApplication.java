package com.airbook.airbook;

import org.mybatis.spring.annotation.MapperScan; // 注意引入
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.airbook.airbook.mapper") // <--- 加上这一行！指定你的 mapper 包路径
public class AirbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirbookApplication.class, args);
    }

}