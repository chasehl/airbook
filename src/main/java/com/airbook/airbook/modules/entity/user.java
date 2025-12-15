package com.airbook.airbook.modules.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity //告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "user") //@Table来指定和哪个数据表对应;如果省略默认表名就是user；

public class user implements Serializable {

    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private int id;

    @Column(name = "uname",length = 50) //这是和数据表对应的一个列
    private String uname;

    @Column(name = "upass",length = 50) //这是和数据表对应的一个列
    private String upass;

    @Column(name = "reupass",length = 50) //这是和数据表对应的一个列
    private String reupass;

    @Column(name = "ucity",length = 50) //这是和数据表对应的一个列
    private String ucity;

    @Column(name = "usex",length = 50) //这是和数据表对应的一个列
    private String usex;

    public String getReupass() {
        return reupass;
    }

    public void setReupass(String reupass) {
        this.reupass = reupass;
    }

    public String getUcity() {
        return ucity;
    }

    public void setUcity(String ucity) {
        this.ucity = ucity;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", upass='" + upass + '\'' +
                ", reupass='" + reupass + '\'' +
                ", ucity='" + ucity + '\'' +
                ", usex='" + usex + '\'' +
                '}';
    }

}
