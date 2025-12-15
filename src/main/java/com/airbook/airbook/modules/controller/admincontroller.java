package com.airbook.airbook.modules.controller;


import com.airbook.airbook.modules.entity.admin;
import com.airbook.airbook.modules.entity.air;
import com.airbook.airbook.modules.web.adminservice;
import com.airbook.airbook.modules.web.airservice;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("admin")
public class admincontroller {
    @Autowired
    private adminservice adminservice;
    @Autowired
    private airservice airservice;


    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public String login(admin admin, HttpServletResponse response) throws IOException {

        String adminname=admin.getAdminname();
        String adminpassword=admin.getAdminpassword();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        if(!adminservice.existsByadminname(adminname)){
            out.print("<script language=\"javascript\">alert('管理员不存在！');window.location.href='/'</script>");
            return "airbook";
        }
        else{
            admin u=adminservice.findName(adminname);
            if(!u.getAdminpassword().equals(adminpassword)){
                out.print("<script language=\"javascript\">alert('密码不正确！');window.location.href='/'</script>");
                return "airbook";
            }
            else{
                out.print("<script language=\"javascript\">alert('登陆成功！');window.location.href='/airbook/admin'</script>");
                return "admin";
            }
        }

    }

    @RequestMapping(value="/changeair",method = RequestMethod.POST)
    public String changeair(air air,HttpServletResponse response)throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        airservice.saveair(air);
        out.print("<script language=\"javascript\">alert('修改成功，返回更改界面！');window.location.href='/airbook/admin'</script>");
        return "admin";
    }

    @RequestMapping(value="/addair",method = RequestMethod.POST)
    public String addair(air air, String place1, String place2, HttpServletResponse response)throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        air.setAplace(place1+"-"+place2);
        airservice.saveair(air);
        out.print("<script language=\"javascript\">alert('添加成功，返回更改界面！');window.location.href='/airbook/admin'</script>");
        return "admin";
    }

    @RequestMapping(value="/delete")
    public String delete(int id,HttpServletResponse response)throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        airservice.delete(id);
        out.print("<script language=\"javascript\">alert('删除成功，返回更改界面！');window.location.href='/airbook/admin'</script>");
        return "admin";
    }

}
