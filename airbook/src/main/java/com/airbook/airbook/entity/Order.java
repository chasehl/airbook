package com.airbook.airbook.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sys_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String orderNo;     // 订单号
    private Integer userId;     // 用户ID
    private Integer flightId;   // 航班ID
    private String flightInfo;  // 航班信息快照
    private BigDecimal price;   // 价格
    private Integer status;     // 0:正常 1:退票
    private Date createTime;    // 创建时间
    private String seatNo;   //座位号
}