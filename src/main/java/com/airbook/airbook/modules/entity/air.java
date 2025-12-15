package com.airbook.airbook.modules.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Entity
@Table(name = "air")

public class air implements Serializable {



    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private int id;

    @Column(name = "anum",length = 50) //这是和数据表对应的一个列
    private String anum;

    @Column(name = "aplace",length = 50) //这是和数据表对应的一个列
    private String aplace;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String atime;

    @Column(name = "acompany",length = 50) //这是和数据表对应的一个列
    private String acompany;

    @Column(name = "aleft",length = 50) //这是和数据表对应的一个列
    private String aleft;

    public float getAprice() {
        return aprice;
    }

    public void setAprice(float aprice) {
        this.aprice = aprice;
    }

    @Column(name = "aprice",length = 10) //这是和数据表对应的一个列
    private float aprice;

/*
    public String changeType(Date atime){
        SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(atime);
    }*/

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

    public String getAtime() { return atime; }

    public void setAtime(String atime){ this.atime= atime;}

    public String getAcompany() {
        return acompany;
    }

    public void setAcompany(String acompany) {
        this.acompany = acompany;
    }

    public String getAleft() {
        return aleft;
    }

    public void setAleft(String aleft) {
        this.aleft = aleft;
    }






    @Override
    public String toString() {
        return "air{" +
                "id=" + id +
                ", anum='" + anum + '\'' +
                ", aplace='" + aplace + '\'' +
                ", atime='" + atime + '\'' +
                ", acompany='" + acompany + '\'' +
                ", aleft='" + aleft + '\'' +
                '}';
    }
}
