package com.springboot_shiro.repository;

import com.springboot_shiro.model.User;

public interface UserMapper {

    User findByName(String name);

    User findById(Integer id);
}
