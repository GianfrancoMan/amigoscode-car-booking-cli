package com.gianfrancomanca.user;

import com.gianfrancomanca.util.FileManager;
import java.util.Arrays;
import java.util.Optional;

public class UserFileDataSourceService implements UserDao {
    private final User[] users;
    private final String filePath;

    public UserFileDataSourceService(String filePath) {
        this.filePath = filePath;
        users = (User[]) FileManager.readFromFile(filePath);
        System.out.println(Arrays.toString(users));
    }


    @Override
    public User[] getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(String id) {
        for(User user : getUsers()) {
            if(user.getId().toString().equals(id)) return Optional.of(user);
        }
        return Optional.empty();
    }
}
