package com.llw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.llw.dto.DishDto;
import com.llw.pojo.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作dish和dish_flavor表
    public void saveWithFlavor(DishDto dishDto);

    //自定义用多表联查实现分页查询
    public Page<DishDto> myPage(Integer page, Integer pageSize, String name);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);
}
