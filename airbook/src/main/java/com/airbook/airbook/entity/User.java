package com.airbook.airbook.entity;

import lombok.Data;

@Data // Lombok 注解，自动生成 Getter/Setter/ToString
public class User {
    private Integer id;
    private String uname;    // 用户名
    private String upass;    // 密码
    private String ucity;    // 城市
    private String usex;     // 性别
}