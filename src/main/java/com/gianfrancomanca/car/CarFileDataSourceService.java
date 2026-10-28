package com.gianfrancomanca.car;

import com.gianfrancomanca.util.FileManager;

import java.io.*;
import java.util.*;

public class CarFileDataSourceService implements CarDao {
    private final  List<Car> cars;
    private final String filePath;

    public CarFileDataSourceService(String filePath) {
        this.filePath = filePath;
        cars = (List<Car>)FileManager.readFromFile(filePath);
    }


    @Override
    public List<Car> getCars() {
        return (null == cars) ? Collections.emptyList() : cars;
    }

    @Override
    public Optional<Car> getCarById(String id) {
        for(Car car : cars) {
            if(car.getId().toString().equals(id)) return Optional.of(car);
        }
        return Optional.empty();
    }
}
