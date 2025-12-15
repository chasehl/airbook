package com.airbook.airbook.modules.controller;

import com.airbook.airbook.modules.dao.airdao;
import com.airbook.airbook.modules.entity.air;
import com.airbook.airbook.modules.entity.user;
import com.airbook.airbook.modules.web.airservice;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller

public class aircontroller   {
    @Autowired
    airservice airservice;
    airdao airdao;

    @RequestMapping("/")
    public String form(Model model){
        List<air> air=new ArrayList<air>();
        air= airservice.findall();
        model.addAttribute("air",air);
        return "airbook";
    }

    @RequestMapping(value = "/anum" , method = RequestMethod.POST)//根据航班号查询
    @ResponseBody
    public String anum(String anum,HttpServletResponse response){


        if(airservice.existsByAnum(anum)){
        List<air> list=new ArrayList<>();
        list=airservice.findallanum(anum);
        String json= JSON.toJSONString(list);
        System.out.println(json);
        return json;}
        else {
            return null;
        }
    }

    @RequestMapping(value = "/aplace" , method = RequestMethod.POST)//根据地点查询
    @ResponseBody
    public String aplace(String aplace,HttpServletResponse response){


        if(airservice.existsByAplace(aplace)){
        List<air> list=new ArrayList<>();
        list=airservice.findallaplace(aplace);
        String json= JSON.toJSONString(list);
        return json;}
        else{
            return null;
        }

    }

    @RequestMapping(value = "/acompany" , method = RequestMethod.POST)//根据公司查询
    @ResponseBody
    public String acompany(String acompany,HttpServletResponse response){


        if(airservice.existsByAcompany(acompany)){
            List<air> list=new ArrayList<>();
            list=airservice.findallacompany(acompany);
            String json= JSON.toJSONString(list);
            System.out.println(json);
            return json;}
        else{
            return null;
        }

    }


   @RequestMapping(value = "/atime" , method = RequestMethod.POST)//根据日期查询
    @ResponseBody
    public String atime(String atime, HttpServletResponse response){


       if(airservice.existsByAtime(atime)){
            List<air> list=new ArrayList<>();
            list=airservice.findallatime(atime);
            String json= JSON.toJSONString(list);
            return json;}
        else{
            return null;
        }

    }

    @RequestMapping(value = "/findall" , method = RequestMethod.POST)//查询所有
    @ResponseBody
    public String findall(String atime, HttpServletResponse response) {
        List<air> list = new ArrayList<>();
        list = airservice.findall();
        String json = JSON.toJSONString(list);
        return json;
    }





}
