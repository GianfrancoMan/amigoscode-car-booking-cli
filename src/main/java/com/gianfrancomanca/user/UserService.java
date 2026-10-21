package com.gianfrancomanca.user;

import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserById(String id) {
        return userDao.getUserById(id).orElse(new User("Not Found", UUID.fromString("87dbde4e-b1f0-4561-8d07-53a31000be68")));
    }

    public User[] getAllUsers() {
        return userDao.getUsers();
    }

}
