package com.gianfrancomanca.user;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> getUserById(String id) {
        return userDao.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getUsers();
    }

}
