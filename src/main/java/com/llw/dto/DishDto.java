package com.llw.dto;

import com.llw.pojo.Dish;
import com.llw.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 继承菜品实体类，用于扩展前端传递的口味列表
 */
@Data
public class DishDto extends Dish {

    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
