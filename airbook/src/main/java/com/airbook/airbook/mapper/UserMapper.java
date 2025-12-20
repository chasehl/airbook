package com.airbook.airbook.mapper;

import com.airbook.airbook.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 1. 登录查账号密码 (这一步你之前可能已经在 Controller 里用了 MP 的 wrapper，或者我们手写一个稳妥的)
    @Select("SELECT * FROM user WHERE uname = #{uname} AND upass = #{upass}")
    User login(String uname, String upass);

    // 2. 注册前查重：看看这个用户名是不是已经存在了
    @Select("SELECT * FROM user WHERE uname = #{uname}")
    User selectByUname(String uname);

    // 3. 注册：插入新用户
    @Insert("INSERT INTO user(uname, upass, ucity, usex, role, create_time) VALUES(#{uname}, #{upass}, #{ucity}, '保密', 0, NOW())")
    int insertUser(User user);
}