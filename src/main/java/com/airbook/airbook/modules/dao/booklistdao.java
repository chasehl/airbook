package com.airbook.airbook.modules.dao;

import com.airbook.airbook.modules.entity.booklist;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MapperScan
public interface booklistdao extends JpaRepository<booklist,Integer> {

       List<booklist> findAllByUsername(String name);
       booklist findAllById(int id);

}
