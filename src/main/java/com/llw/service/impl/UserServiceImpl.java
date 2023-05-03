package com.llw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llw.mapper.UserMapper;
import com.llw.pojo.User;
import com.llw.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
