package com.llw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.llw.dto.SetmealDto;
import com.llw.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增套餐，同时需要保存套餐与菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐，同时删除套餐与菜品关联的数据
    public void removeWithDish(List<Long> ids);
}
