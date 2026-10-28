package com.gianfrancomanca.util;

import java.io.*;
import java.util.List;

public class FileManager {

    public static <T> void writeToFile(List<T> objects, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(objects);
        } catch (IOException e) {
            System.out.println("exception occurred");
        }
    }

    public static Object readFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
           return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("exception occurred\n" + e );
        }
        return null;
    }
}
