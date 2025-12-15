package com.airbook.airbook.modules.controller;



import com.airbook.airbook.modules.entity.air;
import com.airbook.airbook.modules.entity.user;
import com.airbook.airbook.modules.web.airservice;
import com.airbook.airbook.modules.web.booklistservice;
import com.airbook.airbook.modules.web.userservice;
import com.airbook.airbook.modules.web.waitservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("airbook")
public class usercontroller {
    @Autowired
    userservice userservice;
    @Autowired
    airservice airservice;
    @Autowired
    booklistservice booklistservice;
    @Autowired
    waitservice waitservice;



    @RequestMapping(value = "/regist" ,method = RequestMethod.POST)
    public String regist(@ModelAttribute(value="user")user user, HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String upass=user.getUpass();
        String reupass=user.getReupass();
        String uname=user.getUname();


        if(upass.equals(reupass)&&!userservice.existsByUname(uname)){
            userservice.setUser(
                    user.getId(),
                    user.getUname(),
                    user.getUpass(),
                    user.getReupass(),
                    user.getUcity(),
                    user.getUsex());
            out.print("<script language=\"javascript\">alert('注册成功！');window.location.href='/'</script>");
            return "airbook";
        }
        else if(!upass.equals(reupass)){
            out.print("<script language=\"javascript\">alert('输入密码不相符！');window.location.href='/'</script>");
            return "airbook";
        }
        else{
            out.print("<script language=\"javascript\">alert('用户已存在！');window.location.href='/'</script>");
            return "airbook";
        }

    }



    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public String login(@ModelAttribute(value="user")user user, HttpServletResponse response) throws IOException {

        String username=user.getUname();
        String password=user.getUpass();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        if(!userservice.existsByUname(username)){
            out.print("<script language=\"javascript\">alert('用户不存在！');window.location.href='/'</script>");
            return "airbook";
        }
        else{
            user u=userservice.findName(username);
            if(!u.getUpass().equals(password)){
                out.print("<script language=\"javascript\">alert('密码不正确！');window.location.href='/'</script>");
                return "airbook";
            }
            else{
                out.print("<script language=\"javascript\">alert('登陆成功！');window.location.href='/airbook/home'</script>");
                return "home";
            }
        }

    }

    @RequestMapping("/home")
    public String user(Model model){
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        return "home";
    }
    @RequestMapping("/userbooking")
    public String userbooking(Model model){

        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));

        List<air> air=new ArrayList<air>();
        air= airservice.findall();
        model.addAttribute("air",air);

        return "userbooking";
    }

/*
    @RequestMapping("/userserach")
    public String userserach(Model model){

//        List<user> user=userservice.getUser();
//        model.addAttribute("user",user);
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        List<air> air=new ArrayList<air>();
        air= airservice.findall();
        model.addAttribute("air",air);
        return "userserach";
    }*/

    @RequestMapping("/userlist")
    public String userlist(Model model){

        model.addAttribute("userlist",booklistservice.findbyusername(userservice.getUser().getUname()));
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        return "userlist";
    }
    @RequestMapping("/userask")
    public String userask(Model model){
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        return "userask";
    }
    @RequestMapping("/userset")
    public String userset(Model model){
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        return "userset";
    }

    @RequestMapping("/book")
    public String book(int id,Model model,HttpServletResponse response) throws IOException {


        user u=userservice.getUser();
        air a=airservice.findallid(id);
        int aleft=Integer.parseInt(a.getAleft());
        aleft=aleft-1;
        a.setAleft(String.valueOf(aleft));
        airservice.saveair(a);
        booklistservice.setbooklist(u,a);
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script language=\"javascript\">alert('订票成功，进入订单界面！');window.location.href='/airbook/userlist'</script>");
        return "userlist";
    }

    @RequestMapping("/wait")
    public String wait(int id,Model model,HttpServletResponse response) throws IOException {
        user u=userservice.getUser();
        air a=airservice.findallid(id);
        waitservice.setwait(u,a);
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script language=\"javascript\">alert('抢票成功，进入订单界面！');window.location.href='/airbook/userlist'</script>");
        return "userlist";
    }

    @RequestMapping("/unbook")
    public String unbook(int aid,int uid,int id,Model model,HttpServletResponse response) throws IOException {
        model.addAttribute("user",userservice.findUser(userservice.getUser().getId()));
        String anum=booklistservice.findanumbyid(id);
        if(waitservice.existanum(anum)==1){

        }//删除第一个等待，并赋予订票
        else {
            air a=airservice.findallid(aid);
            int aleft=Integer.parseInt(a.getAleft());
            aleft=aleft+1;
            a.setAleft(String.valueOf(aleft));
            airservice.saveair(a);
        }

        booklistservice.delete(id);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script language=\"javascript\">alert('退票成功，返回订单界面！');window.location.href='/airbook/userlist'</script>");
        return "userlist";
    }



    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }



}
