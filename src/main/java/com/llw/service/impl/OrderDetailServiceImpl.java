package com.llw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llw.mapper.OrderDetailMapper;
import com.llw.pojo.OrderDetail;
import com.llw.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}