package com.gianfrancomanca.car;

import com.gianfrancomanca.util.FileManager;

import java.io.*;
import java.util.Optional;

public class CarFileDataSourceService implements CarDao {
    private final  Car[] cars;
    private final String filePath;

    public CarFileDataSourceService(String filePath) {
        this.filePath = filePath;
        cars = (Car[])FileManager.readFromFile(filePath);
    }


    @Override
    public Car[] getCars() {
        return cars;
    }

    @Override
    public Optional<Car> getCarById(String id) {
        for(Car car : cars) {
            if(car.getId().toString().equals(id)) return Optional.of(car);
        }
        return Optional.empty();
    }

}
