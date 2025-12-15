package com.airbook.airbook.modules.entity;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin")

public class admin implements Serializable {

    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键

    @Column(name = "adminpassword",length = 255) //这是和数据表对应的一个列
    private String adminpassword;

    @Column(name = "adminname",length = 255) //这是和数据表对应的一个列
    private String adminname;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }


}
