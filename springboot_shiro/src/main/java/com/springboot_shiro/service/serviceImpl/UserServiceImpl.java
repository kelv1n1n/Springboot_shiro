package com.springboot_shiro.service.serviceImpl;

import com.springboot_shiro.model.User;
import com.springboot_shiro.repository.UserMapper;
import com.springboot_shiro.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User findByName(String name) {
        return userMapper.findByName(name);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
}
