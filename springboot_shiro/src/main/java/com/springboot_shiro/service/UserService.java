package com.springboot_shiro.service;

import com.springboot_shiro.model.User;

public interface UserService {

    User findByName(String name);

    User findById(Integer id);
}
