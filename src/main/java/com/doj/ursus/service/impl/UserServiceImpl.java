package com.doj.ursus.service.impl;

import com.doj.ursus.dao.UserDao;
import com.doj.ursus.model.User;
import com.doj.ursus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public void insert(User user) {

    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User findUserById(int id) {
        return null;
    }

    @Override
    public List findAll() {
        return userDao.findAll();
    }
}
