package com.doj.ursus.dao;

import com.doj.ursus.model.User;

import java.util.List;

public interface UserDao {

    public void insert(User user);
    public User findById(int id);
    User create(User user);
    User findUserById(int id);
    List findAll();
}
