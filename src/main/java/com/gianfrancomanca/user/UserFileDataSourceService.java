package com.gianfrancomanca.user;

import com.gianfrancomanca.util.FileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class UserFileDataSourceService implements UserDao {
    private final List<User> users;
    private final String filePath;

    public UserFileDataSourceService(String filePath) {
        this.filePath = filePath;
        users = (List<User>) FileManager.readFromFile(filePath);
        System.out.println(users); //TODO remember to cancel it when project is completed
    }


    @Override
    public List<User> getUsers() {
        return null == users ? Collections.emptyList() : users;
    }

    @Override
    public Optional<User> getUserById(String id) {
        for(User user : getUsers()) {
            if(user.getId().toString().equals(id)) return Optional.of(user);
        }
        return Optional.empty();
    }
}
