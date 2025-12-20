package com.airbook.airbook.mapper;

import com.airbook.airbook.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update; // 引入 Update
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Insert("INSERT INTO sys_order(order_no, user_id, flight_id, flight_info, price, status, seat_no, create_time) " +
            "VALUES(#{orderNo}, #{userId}, #{flightId}, #{flightInfo}, #{price}, 0, #{seatNo}, NOW())")
    int createOrder(Order order);

    @Select("SELECT * FROM sys_order WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> selectByUserId(Integer userId);

    // ▼▼▼ 新增：根据ID查找订单 ▼▼▼
    @Select("SELECT * FROM sys_order WHERE id = #{id}")
    Order selectById(Integer id);

    // ▼▼▼ 新增：取消订单 (把状态变成 1) ▼▼▼
    @Update("UPDATE sys_order SET status = 1 WHERE id = #{id}")
    int cancelOrder(Integer id);

    // ▼▼▼ 新增：查询所有订单 (管理员用) ▼▼▼
    @Select("SELECT * FROM sys_order ORDER BY create_time DESC")
    List<Order> selectAll();
}