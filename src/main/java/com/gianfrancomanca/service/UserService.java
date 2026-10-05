package com.gianfrancomanca.service;

import com.gianfrancomanca.dao.UserDao;
import com.gianfrancomanca.model.User;

public class UserService {

    private UserDao userDao = new UserDao();

    public User getUserById(String id) {
        return userDao.getUserById(id).orElse(new User("Not Found"));
    }

    public User[] getAllUsers() {
        return userDao.getUsers();
    }

}
