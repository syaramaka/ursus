package com.doj.ursus.controller;

import com.doj.ursus.impl.UserDaoImpl;
import com.doj.ursus.model.User;
import com.doj.ursus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDaoImpl userDao;

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/user/getAll")
    public List<User> getAll() {
        logger.debug("Getting all files details from the database.");
        System.out.println("Getting all files details from the database.");
        //return userDao.findAll();
        return userService.findAll();
    }


}
