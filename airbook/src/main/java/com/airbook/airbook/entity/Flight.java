package com.airbook.airbook.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
import java.math.BigDecimal; // 用于处理价格

@Data
@TableName("sys_flight") // 告诉 MyBatis Plus 对应数据库哪张表
public class Flight {
    private Integer id;
    private String flightNo;      // 航班号 (如 MU5566)
    private String departure;     // 出发地
    private String destination;   // 目的地
    private Date startTime;       // 起飞时间
    private Date endTime;         // 降落时间
    private String airline;       // 航空公司
    private BigDecimal price;     // 价格
    private Integer totalSeats;   // 总座位
    private Integer availableSeats; // 剩余座位
    private String occupiedSeats;  //已售出的座位集合
}