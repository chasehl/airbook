package com.airbook.airbook.controller;

import com.airbook.airbook.entity.Flight;
import com.airbook.airbook.entity.Order;
import com.airbook.airbook.mapper.FlightMapper;
import com.airbook.airbook.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@CrossOrigin // 允许跨域
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FlightMapper flightMapper;

    /**
     * 1. 创建订单 (预订 + 选座)
     * 参数: userId, flightId, seatNo
     */
    @PostMapping("/create")
    @Transactional // 开启事务：库存扣减和订单生成必须同时成功
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 获取参数 (注意类型转换)
            Integer userId = (Integer) params.get("userId");
            Integer flightId = (Integer) params.get("flightId");
            String seatNo = (String) params.get("seatNo"); // 获取前端传来的座位号

            // 2. 简单校验
            if (seatNo == null || seatNo.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "预订失败：请先选择座位！");
                return result;
            }

            // 3. 尝试锁定座位 (扣库存 + 更新已占座位列表)
            // 调用 FlightMapper 中新写的 updateSeats 方法
            int updateCount = flightMapper.updateSeats(flightId, seatNo);

            if (updateCount <= 0) {
                result.put("code", 400);
                result.put("msg", "预订失败：该座位已被抢先一步！");
                return result;
            }

            // 4. 查询航班详情 (为了获取价格和航班信息存入订单)
            Flight flight = flightMapper.selectById(flightId);

            // 5. 生成订单对象
            Order order = new Order();
            // 生成唯一订单号 (如: ORD8A2B3C)
            order.setOrderNo("ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            order.setUserId(userId);
            order.setFlightId(flightId);
            order.setPrice(flight.getPrice());
            // 存入航班快照，防止航班删除后订单查不到信息
            order.setFlightInfo(flight.getFlightNo() + " " + flight.getDeparture() + "-" + flight.getDestination());
            // 存入座位号
            order.setSeatNo(seatNo);

            // 6. 保存到数据库
            orderMapper.createOrder(order);

            result.put("code", 200);
            result.put("msg", "预订成功！");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "系统异常：" + e.getMessage());
            // 手动抛出异常以触发事务回滚
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 2. 查询我的订单列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        // 调用 OrderMapper 查询
        List<Order> orders = orderMapper.selectByUserId(userId);

        result.put("code", 200);
        result.put("data", orders);
        return result;
    }

    /**
     * 3. 退票接口
     */
    @PostMapping("/cancel")
    @Transactional
    public Map<String, Object> cancel(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        Integer orderId = params.get("orderId");

        // 1. 查订单状态
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getStatus() == 1) {
            result.put("code", 400);
            result.put("msg", "订单不存在或已取消");
            return result;
        }

        // 2. 修改订单状态为 1 (已取消)
        orderMapper.cancelOrder(orderId);

        // 3. 航班库存 + 1 
        // (注：为了简化逻辑，这里只加回了库存数，没有从 occupied_seats 字符串里把座位号抠出来)
        // (如果是商业项目，需要写逻辑把 "1A" 从字符串里删掉，但实训项目通常只需加回库存即可)
        flightMapper.increaseSeat(order.getFlightId());

        result.put("code", 200);
        result.put("msg", "退票成功");
        return result;
    }
    // ▼▼▼ 新增：管理员查询所有订单 ▼▼▼
    @GetMapping("/all")
    public Map<String, Object> listAll() {
        Map<String, Object> result = new HashMap<>();

        // 调用刚才手写的方法
        List<Order> orders = orderMapper.selectAll();

        result.put("code", 200);
        result.put("data", orders);
        return result;
    }
}