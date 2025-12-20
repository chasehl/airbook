package com.airbook.airbook.mapper;

import com.airbook.airbook.entity.Flight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlightMapper extends BaseMapper<Flight> {

    // === 原有查询方法 ===
    @Select("SELECT * FROM sys_flight")
    List<Flight> searchAll();

    @Select("SELECT * FROM sys_flight WHERE flight_no LIKE CONCAT('%',#{keyword},'%') OR departure LIKE CONCAT('%',#{keyword},'%') OR destination LIKE CONCAT('%',#{keyword},'%')")
    List<Flight> searchByKeyword(String keyword);

    @Select("SELECT * FROM sys_flight WHERE id = #{id}")
    Flight selectById(Integer id);

    // === 原有库存方法 ===
    @Update("UPDATE sys_flight SET available_seats = available_seats - 1 WHERE id = #{id} AND available_seats > 0")
    int decreaseSeat(Integer id);

    @Update("UPDATE sys_flight SET available_seats = available_seats + 1 WHERE id = #{id}")
    int increaseSeat(Integer id);

    @Update("UPDATE sys_flight SET available_seats = available_seats - 1, occupied_seats = CONCAT(IFNULL(occupied_seats, ''), ',', #{seatNo}) WHERE id = #{id} AND available_seats > 0")
    int updateSeats(@Param("id") Integer id, @Param("seatNo") String seatNo);

    // ▼▼▼▼▼▼ 新增：手动写的新增和修改 (解决报错的核心) ▼▼▼▼▼▼

    // 1. 手动新增航班
    @Insert("INSERT INTO sys_flight(flight_no, airline, departure, destination, start_time, end_time, price, total_seats, available_seats, occupied_seats) " +
            "VALUES(#{flightNo}, #{airline}, #{departure}, #{destination}, #{startTime}, #{endTime}, #{price}, #{totalSeats}, #{availableSeats}, #{occupiedSeats})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 让ID自增
    int manualInsert(Flight flight);

    // 2. 手动更新航班
    @Update("UPDATE sys_flight SET flight_no=#{flightNo}, airline=#{airline}, departure=#{departure}, destination=#{destination}, " +
            "start_time=#{startTime}, end_time=#{endTime}, price=#{price}, total_seats=#{totalSeats} WHERE id=#{id}")
    int manualUpdate(Flight flight);

    // 3. 手动删除航班
    @Delete("DELETE FROM sys_flight WHERE id = #{id}")
    int manualDelete(Integer id);
}