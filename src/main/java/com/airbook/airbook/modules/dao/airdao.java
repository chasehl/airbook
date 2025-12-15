package com.airbook.airbook.modules.dao;

import com.airbook.airbook.modules.entity.air;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@MapperScan

public interface airdao extends JpaRepository<air,Integer> {

    List<air> findAllByAnum(String anum);
    List<air> findAllByAplace(String aplace);
    List<air> findAllByAtime(String atime);
    List<air> findAllByAcompany(String acompany);
    air findAllById(int id);

    boolean existsByAnum(String anum);
    boolean existsByAplace(String aplace);
    boolean existsByAtime(String atime);
    boolean existsByAcompany(String acompany);


}
