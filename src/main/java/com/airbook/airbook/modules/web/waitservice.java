package com.airbook.airbook.modules.web;

import com.airbook.airbook.modules.dao.waitdao;
import com.airbook.airbook.modules.entity.air;
import com.airbook.airbook.modules.entity.booklist;
import com.airbook.airbook.modules.entity.user;
import com.airbook.airbook.modules.entity.wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class waitservice {
    @Autowired waitdao waitdao;

    wait wait=new wait();
    public void setwait(user u, air a)

    {

        wait.setUsername(u.getUname());
        wait.setAnum(a.getAnum());
        wait.setAplace(a.getAplace());
        wait.setAtime(a.getAtime());
        wait.setAcompany(a.getAcompany());
        wait.setAprice(a.getAprice());
        waitdao.saveAndFlush(wait);//jpa语句
    }

    public int existanum(String anum){
        if(waitdao.existsByAnum(anum))return 1;
        else return 0;
    }

}
