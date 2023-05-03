package com.llw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llw.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
