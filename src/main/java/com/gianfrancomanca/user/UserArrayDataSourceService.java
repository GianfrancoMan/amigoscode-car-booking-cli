package com.gianfrancomanca.user;

import java.util.*;

public class UserArrayDataSourceService implements UserDao {
    public static List<User> users = new ArrayList<>();

    static  {
        users.add(new User("Gianfranco Manca", UUID.fromString("d6d0a6fc-a406-4f00-969a-7f5bc16b97cb")));
        users.add(new User("Marco Polo", UUID.fromString("3e07bd0e-3bc8-42ae-849f-2052b7a73600")));
        users.add(new User("Giuseppe Verdi", UUID.fromString("48a60e10-0686-418f-9d61-c2264dd372f8")));
        users.add(new User("Giorgio Gaber", UUID.fromString("ad660246-c985-4367-90e5-f20b5d467f9f")));
        users.add(new User("Francesco Totti", UUID.fromString("ba49495a-3bde-494b-96dd-9f3a668d7dfc")));
    }


    @Override
    public List<User> getUsers() {
        return (null == users) ? Collections.emptyList() : users;
    }

    @Override
    public Optional<User> getUserById(String id) {
        for(User user : getUsers()) {
            if(user.getId().toString().equals(id)) return Optional.of(user);
        }
        return Optional.ofNullable(null);
    }
}
