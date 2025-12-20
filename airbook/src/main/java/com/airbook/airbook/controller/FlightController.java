package com.airbook.airbook.controller;

import com.airbook.airbook.entity.Flight;
import com.airbook.airbook.mapper.FlightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flight")
@CrossOrigin // 允许前端跨域访问，必须加！
public class FlightController {

    @Autowired
    private FlightMapper flightMapper;

    /**
     * 查询航班列表接口
     * URL: /api/flight/list?keyword=xxx
     */
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        List<Flight> flights;

        try {
            // 判断前端是否传了搜索关键词
            if (keyword == null || keyword.trim().isEmpty()) {
                // 1. 如果没传词，调用我们刚才手写的 searchAll() 查全部
                flights = flightMapper.searchAll();
            } else {
                // 2. 如果传了词，调用我们刚才手写的 searchByKeyword() 查匹配
                flights = flightMapper.searchByKeyword(keyword);
            }

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", flights);
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误到控制台，方便调试
            result.put("code", 500);
            result.put("msg", "服务器内部错误: " + e.getMessage());
        }

        return result;
    }
    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody Flight flight) {
        Map<String, Object> result = new HashMap<>();

        if (flight.getId() == null) {
            // 新增逻辑
            flight.setAvailableSeats(flight.getTotalSeats());
            flight.setOccupiedSeats("");
            // ❌ 之前是: flightMapper.insert(flight);
            // ✅ 改为调用刚才手写的:
            flightMapper.manualInsert(flight);
        } else {
            // 修改逻辑
            // ❌ 之前是: flightMapper.updateById(flight);
            // ✅ 改为调用刚才手写的:
            flightMapper.manualUpdate(flight);
        }

        result.put("code", 200);
        result.put("msg", "保存成功");
        return result;
    }

    // 修改 delete 方法
    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        Integer id = params.get("id");

        // ❌ 之前是: flightMapper.deleteById(id);
        // ✅ 改为调用刚才手写的:
        flightMapper.manualDelete(id);

        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }
}