package com.doj.ursus.service;

import com.doj.ursus.model.User;

import java.util.List;

public interface UserService {
    
    public void insert(User user);
    public User findById(int id);
    User create(User user);
    User findUserById(int id);
    List findAll();
}
