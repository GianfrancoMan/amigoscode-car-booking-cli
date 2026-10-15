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

    //used one time to store seeded users
//    static void main() {
//        User[] users = new User[5];
//        users[0] = new User("Gianfranco Manca", UUID.fromString("d6d0a6fc-a406-4f00-969a-7f5bc16b97cb"));
//        users[1] = new User("Marco Polo", UUID.fromString("3e07bd0e-3bc8-42ae-849f-2052b7a73600"));
//        users[2] = new User("Giuseppe Verdi", UUID.fromString("48a60e10-0686-418f-9d61-c2264dd372f8"));
//        users[3] = new User("Giorgio Gaber", UUID.fromString("ad660246-c985-4367-90e5-f20b5d467f9f"));
//        users[4] = new User("Francesco Totti", UUID.fromString("ba49495a-3bde-494b-96dd-9f3a668d7dfc"));
//
//        try (FileOutputStream fos = new FileOutputStream("src/main/resources/user-file-data-source.cs"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            System.out.println("Writing to file...");
//            oos.writeObject(users);
//            System.out.println("Done.");
//        } catch (IOException e) {
//            System.out.println("exception occurred" + e);
//        }
//    }
}
