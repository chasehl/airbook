package com.airbook.airbook.modules.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

@Entity
@Table(name = "wait")

public class wait {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAnum() {
        return anum;
    }

    public void setAnum(String anum) {
        this.anum = anum;
    }

    public String getAplace() {
        return aplace;
    }

    public void setAplace(String aplace) {
        this.aplace = aplace;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getAcompany() {
        return acompany;
    }

    public void setAcompany(String acompany) {
        this.acompany = acompany;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private int id;

    @Column(name = "username",length = 50) //这是和数据表对应的一个列
    private String username;

    @Column(name = "anum",length = 50) //这是和数据表对应的一个列
    private String anum;
    @Column(name = "aplace",length = 50) //这是和数据表对应的一个列
    private String aplace;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String atime;

    @Column(name = "acompany",length = 50) //这是和数据表对应的一个列
    private String acompany;

    @Column(name = "aprice",length = 10) //这是和数据表对应的一个列
    private float aprice;

    public float getAprice() {
        return aprice;
    }

    public void setAprice(float aprice) {
        this.aprice = aprice;
    }
}
