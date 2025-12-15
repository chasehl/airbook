package com.airbook.airbook.modules.web;

        import com.airbook.airbook.modules.dao.userdao;
        import com.airbook.airbook.modules.entity.user;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestParam;



@Service
public class userservice {

    @Autowired
    private userdao userdao;
    private  user user = new user();

    public void setUser(@PathVariable("id") int id,
                          @RequestParam(value = "uname", required = true) String name,
                          @RequestParam(value = "upass", required = true) String pass,
                          @RequestParam(value = "reupass", required = true) String repass,
                          @RequestParam(value = "ucity", required = true) String city,
                          @RequestParam(value = "usex", required = true) String sex){


        user.setUname(name);
        user.setUpass(pass);
        user.setId(id);
        user.setReupass(repass);
        user.setUcity(city);
        user.setUsex(sex);

        userdao.saveAndFlush(user);//jpa语句

       /* return userlist.toString();*/

    }
    public user getUser(){
        user.getUname();
        user.getId();
        user.getUpass();
        user.getReupass();
        user.getUcity();
        user.getUsex();

        return user;
    }

    public user findUser(@PathVariable("id") int id){

        user=userdao.findById(id).get();
        return user;
}
    public user findName(@RequestParam(value = "uname",required = true)String name){

        user=userdao.findByUname(name);
        return user;

    }
    public boolean existsByUname(@RequestParam(value = "uname",required = true)String name){
        boolean flag=userdao.existsByUname(name);
        return flag;

    }







}
