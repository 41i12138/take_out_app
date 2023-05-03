package com.llw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llw.dto.DishDto;
import com.llw.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    //自定义实现分页查询（多表联查）
    public List<DishDto> myPage(String name);
}
