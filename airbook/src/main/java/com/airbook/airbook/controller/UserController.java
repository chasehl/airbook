package com.airbook.airbook.controller;

import com.airbook.airbook.entity.User;
import com.airbook.airbook.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin // 允许跨域
public class UserController {

    @Autowired
    private UserMapper userMapper;

    // === 登录接口 ===
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        // 使用手写的 SQL 查询，稳准狠
        User dbUser = userMapper.login(user.getUname(), user.getUpass());

        if (dbUser != null) {
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", dbUser);
        } else {
            result.put("code", 400);
            result.put("msg", "用户名或密码错误");
        }
        return result;
    }

    // === 注册接口 (新增) ===
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        // 1. 校验用户名是否为空
        if (user.getUname() == null || user.getUname().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "用户名不能为空");
            return result;
        }

        // 2. 查重
        User existUser = userMapper.selectByUname(user.getUname());
        if (existUser != null) {
            result.put("code", 400);
            result.put("msg", "注册失败：该用户名已被占用！");
            return result;
        }

        // 3. 插入数据库
        // 如果前端没传城市，给个默认值
        if (user.getUcity() == null) user.setUcity("未知城市");

        userMapper.insertUser(user);

        result.put("code", 200);
        result.put("msg", "注册成功，请登录！");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        // 1. 检查有没有ID
        if (user.getId() == null) {
            result.put("code", 400);
            result.put("msg", "用户ID不能为空");
            return result;
        }

        // 2. 调用 MyBatis Plus 自带的更新方法
        userMapper.updateById(user);

        // 3. 查出最新数据返回给前端（刷新缓存）
        User updatedUser = userMapper.selectById(user.getId());

        result.put("code", 200);
        result.put("msg", "个人信息修改成功！");
        result.put("data", updatedUser);
        return result;
    }
}