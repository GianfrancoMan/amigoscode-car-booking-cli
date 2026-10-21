package com.gianfrancomanca.user;

import java.util.Optional;

public interface UserDao {

    //retrieve all users
    User[] getUsers();

    //retrieve a user by id
    Optional<User> getUserById(String id);
}
