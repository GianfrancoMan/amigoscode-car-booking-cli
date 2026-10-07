package com.gianfrancomanca.user;

import java.util.Optional;

public class UserDao {
    public static User[] users = new User[5];

    static  {
        users[0] = new User("Gianfranco Manca");
        users[1] = new User("Marco Polo");
        users[2] = new User("Giuseppe Verdi");
        users[3] = new User("Giorgio Gaber");
        users[4] = new User("Francesco Totti");
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
