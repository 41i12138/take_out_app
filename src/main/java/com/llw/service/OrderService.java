package com.llw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.llw.pojo.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
