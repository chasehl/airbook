package com.airbook.airbook.modules.dao;

import com.airbook.airbook.modules.entity.admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface admindao extends JpaRepository<admin,Integer> {

    admin findByAdminname(String adminname);
    boolean existsByAdminname(String adminname);

}
