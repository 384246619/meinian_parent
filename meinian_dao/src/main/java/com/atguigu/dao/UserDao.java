package com.atguigu.dao;

import com.atguigu.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User findUserByUsername(String username);
}
