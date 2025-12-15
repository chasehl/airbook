package com.airbook.airbook.modules.dao;

import com.airbook.airbook.modules.entity.wait;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface waitdao extends JpaRepository<wait,Integer> {
    boolean existsByAnum(String anum);

/*    @Query(value = "", nativeQuery = true)
    void deletewait();*/

}
