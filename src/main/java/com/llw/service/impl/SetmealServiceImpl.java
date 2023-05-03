package com.llw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llw.common.CustomException;
import com.llw.dto.SetmealDto;
import com.llw.mapper.SetmealMapper;
import com.llw.pojo.Setmeal;
import com.llw.pojo.SetmealDish;
import com.llw.service.SetmealDishService;
import com.llw.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishServiceService;

    /**
     * 新增套餐，同时需要保存套餐与菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作Setmeal_dish，执行Insert操作
        setmealDishServiceService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐与菜品关联的数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态是否为停售状态
        //select count(*) from setmeal where id in ( , , ) and status = 1;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        long count = this.count(queryWrapper);

        //如果未处于停售状态，则不可删除抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，则先删除setmeal表中的数据
        this.removeByIds(ids);

        //再删除setmeal_dish表中的关联数据
        //delete from setmeal_dish where setmeal_id in ( , , )
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishServiceService.remove(queryWrapper1);
    }
}
