package com.llw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.llw.pojo.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
