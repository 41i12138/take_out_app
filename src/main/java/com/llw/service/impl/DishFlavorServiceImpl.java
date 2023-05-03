package com.llw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llw.mapper.DishFlavorMapper;
import com.llw.pojo.DishFlavor;
import com.llw.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
