package com.llw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.llw.mapper.ShoppingCartMapper;
import com.llw.pojo.ShoppingCart;
import com.llw.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
