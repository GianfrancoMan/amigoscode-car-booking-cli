package com.gianfrancomanca.user;

public class UserService {

    private UserDao userDao = new UserDao();

    public User getUserById(String id) {
        return userDao.getUserById(id).orElse(new User("Not Found"));
    }

    public User[] getAllUsers() {
        return userDao.getUsers();
    }

}
