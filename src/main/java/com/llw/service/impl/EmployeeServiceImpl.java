package com.llw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llw.mapper.EmployeeMapper;
import com.llw.pojo.Employee;
import com.llw.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
