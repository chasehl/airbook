package com.airbook.airbook.modules.dao;

import com.airbook.airbook.modules.entity.user;
import io.lettuce.core.dynamic.annotation.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MapperScan


public interface userdao extends JpaRepository<user,Integer> {

          user findByUname(String name);
          boolean existsByUname(String name);
}