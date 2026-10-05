package com.gianfrancomanca.dao;

import com.gianfrancomanca.model.User;

import java.util.Arrays;
import java.util.Optional;

public class UserDao {
    public static User[] users = new User[5];

    static  {
        users[0] = new User("Gianfranco Manca");
        users[1] = new User("Marco Polo");
        users[2] = new User("Giuseppe Verdi");
        users[3] = new User("Giorgio Gaber");
        users[4] = new User("Francesco Totti");
        System.out.println(Arrays.toString(users)); //printing the array of users
    }


    //retrieve all users
    public User[] getUsers() {
        return users;
    }

    //retrieve a user by id
    public Optional<User> getUserById(String id) {
        for(User user : getUsers()) {
            if(user.getId().toString().equals(id)) return Optional.of(user);
        }
        return Optional.ofNullable(null);
    }
}
