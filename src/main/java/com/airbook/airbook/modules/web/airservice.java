package com.airbook.airbook.modules.web;

import com.airbook.airbook.modules.dao.airdao;
import com.airbook.airbook.modules.entity.air;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Service
public class airservice {

    @Autowired
    private airdao airdao;

    private air air=new air();

    public void setaid(@PathVariable("id") int id){ air.setId(id);}
    public void setanum(@RequestParam(value = "anum", required = true) String anum){ air.setAnum(anum);}
    public void setaplace(@RequestParam(value = "aplace", required = true) String aplace){ air.setAplace(aplace);}
    public void setatime(@RequestParam(value = "atime", required = true) String atime){ air.setAtime(atime);}
    public void setacompany(@RequestParam(value = "acompany", required = true) String acompany){ air.setAcompany(acompany);}
    public void setaleft(@RequestParam(value = "aleft", required = true) String aleft){ air.setAleft(aleft);}


    public air getair(){
        air.getId();
        air.getAnum();
        air.getAplace();
        air.getAtime();
        air.getAcompany();
        air.getAleft();
        return air;
    }


    public List<air> findall(){
        return airdao.findAll();
    }
    public List<air> findallanum(@RequestParam(value = "anum",required = true)String anum){ return airdao.findAllByAnum(anum);}
    public List<air> findallaplace(@RequestParam(value = "aplace",required = true)String aplace){ return airdao.findAllByAplace(aplace);}
    public List<air> findallatime(@RequestParam(value = "atime",required = true)String atime){ return airdao.findAllByAtime(atime);}
    public List<air> findallacompany(@RequestParam(value = "acompany",required = true)String acompany){ return airdao.findAllByAcompany(acompany);}
    public air findallid(@RequestParam(value = "id",required = true)int id){return airdao.findAllById(id);}

    public boolean existsByAnum(@RequestParam(value = "anum",required = true)String anum){
        boolean flag=airdao.existsByAnum(anum);
        return flag;

    }
    public boolean existsByAplace(@RequestParam(value = "aplace",required = true)String aplace){
        boolean flag=airdao.existsByAplace(aplace);
        return flag;

    }
    public boolean existsByAtime(@RequestParam(value = "atime",required = true)String atime){

        boolean flag=airdao.existsByAtime(atime);
        return flag;

    }
    public boolean existsByAcompany(@RequestParam(value = "acompany",required = true)String acompany){
        boolean flag=airdao.existsByAcompany(acompany);
        return flag;

    }

    public void saveair(@RequestParam(value = "air",required = true)air air){
        airdao.save(air);
    }

    public void delete(int id){
        air= airdao.findAllById(id);
        airdao.delete(air);
    }


}
