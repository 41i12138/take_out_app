package com.llw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llw.common.Result;
import com.llw.dto.DishDto;
import com.llw.pojo.Category;
import com.llw.pojo.Dish;
import com.llw.pojo.DishFlavor;
import com.llw.service.CategoryService;
import com.llw.service.DishFlavorService;
import com.llw.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) { //是DishDto,不是Dish
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询1
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
//    @GetMapping("/page")
//    public Result<Page> page(int page, int pageSize, String name) {
//        //构造分页构造器
//        Page<Dish> pageInfo = new Page<>(page, pageSize);
//        Page<DishDto> dishDtoPage = new Page<>();
//
//        //条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        //添加过滤条件
//        queryWrapper.like(name != null, Dish::getName, name);
//        //添加排序条件
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//
//        //执行分页查询
//        dishService.page(pageInfo, queryWrapper);
//
//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
//
//        List<Dish> records = pageInfo.getRecords();
//        List<DishDto> list = records.stream().map((item) -> {
//            DishDto dishDto = new DishDto();
//
//            BeanUtils.copyProperties(item, dishDto);
//
//            Long categoryId = item.getCategoryId();
//            //根据id查分类对象
//            Category category = categoryService.getById(categoryId);
//            if (category != null) {
//                String categoryName = category.getName();
//                dishDto.setCategoryName(categoryName);
//            }
//
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        dishDtoPage.setRecords(list);
//
//        return Result.success(dishDtoPage);
//    }

    /**
     * 菜品信息分页查询2（自己用多表联查写sql）
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        Page<DishDto> dishDtoPage = dishService.myPage(page, pageSize, name);
        return Result.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish) { //这里前端传的是categoryId，不是dish对象，不需要@RequestBody
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1); //状态为1（起售状态）的菜品
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //添加口味信息
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dtoList);
    }

}
