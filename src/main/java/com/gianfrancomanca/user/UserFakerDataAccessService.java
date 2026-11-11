package com.gianfrancomanca.user;

import net.datafaker.Faker;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFakerDataAccessService implements UserDao{

    private List<User> users;
    private int numberOfUsers;

    public UserFakerDataAccessService(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
        generateUsers();
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return users.stream().filter(user -> user.getId().toString().equals(id)).findFirst();
    }

    private List<User> generateUsers() {
        users = new java.util.ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < numberOfUsers; i++) users.add(new User(faker.name().fullName(), UUID.randomUUID()));
        return users;
    }
}
