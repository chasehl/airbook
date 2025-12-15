package com.airbook.airbook.modules.web;


import com.airbook.airbook.modules.dao.booklistdao;
import com.airbook.airbook.modules.entity.air;
import com.airbook.airbook.modules.entity.booklist;
import com.airbook.airbook.modules.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class booklistservice {
    @Autowired
    private booklistdao booklistdao;
    private booklist booklist= new booklist();


    public void setbooklist(user u, air a)

    {
        booklist.setAid(a.getId());
        booklist.setUid(u.getId());
        booklist.setUsername(u.getUname());
        booklist.setUsercity(u.getUcity());
        booklist.setUsex(u.getUsex());
        booklist.setAnum(a.getAnum());
        booklist.setAplace(a.getAplace());
        booklist.setAtime(a.getAtime());
        booklist.setAcompany(a.getAcompany());
        booklist.setAprice(a.getAprice());
        booklistdao.saveAndFlush(booklist);//jpa语句
    }

    public List<booklist> findbyusername(String username){
        return booklistdao.findAllByUsername(username);
    }


    public void delete(int id){
        booklist= booklistdao.findAllById(id);
        booklistdao.delete(booklist);
    }

    public String findanumbyid(int id) {
        booklist=booklistdao.findAllById(id);
        return booklist.getAnum();
    }
}