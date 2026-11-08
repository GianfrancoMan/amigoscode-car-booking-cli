package com.gianfrancomanca.user;

import com.gianfrancomanca.util.FileManager;

import java.util.*;

public class UserFileDataSourceService implements UserDao {
    private final List<User> users;
    private final String filePath;

    public UserFileDataSourceService(String filePath) {
        this.filePath = filePath;
        users = (List<User>) FileManager.readFromFile(filePath);
    }


    @Override
    public List<User> getUsers() {
        return null == users ? Collections.emptyList() : users;
    }

    @Override
    public Optional<User> getUserById(String id) {
        User resultingUser = users.stream().filter(user -> user.getId().toString().equals(id)).findFirst().orElse(null);
        return null == resultingUser ? Optional.empty() : Optional.of(resultingUser);
    }
}
