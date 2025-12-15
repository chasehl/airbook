package com.airbook.airbook.modules.web;

import com.airbook.airbook.modules.dao.admindao;
import com.airbook.airbook.modules.entity.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class adminservice {
    @Autowired(required=true)
    private admindao admindao;
    private admin admin1=new admin();

    public admin findName(@RequestParam(value = "adminname",required = true)String adminname){
        admin1=admindao.findByAdminname(adminname);
        return admin1;

    }

    public boolean existsByadminname(@RequestParam(value = "adminname",required = true)String adminname){
        boolean flag=admindao.existsByAdminname(adminname);
        return flag;
    }

}
