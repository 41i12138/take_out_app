package com.llw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llw.common.Result;
import com.llw.pojo.Category;
import com.llw.pojo.Employee;
import com.llw.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.catalog.Catalog;
import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize) {
        log.info("page = {}, pageSize = {}", page, pageSize);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        log.info("新增分类: {}", category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long ids) {
        log.info("删除分类, id为:{}", ids);
//        categoryService.removeById(id);
        categoryService.remove(ids);
        return Result.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        log.info("修改分类信息: {}", category);
        categoryService.updateById(category);
        return Result.success("修改分类成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return Result.success(list);
    }
}
