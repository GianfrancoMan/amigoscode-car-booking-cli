package com.gianfrancomanca.car;

import com.gianfrancomanca.util.FileManager;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarFileDataSourceService implements CarDao {
    private final  List<Car> cars;
    private final String filePath;

    public CarFileDataSourceService(String filePath) {
        this.filePath = filePath;
        cars = (List<Car>)FileManager.readFromFile(filePath);
    }


    @Override
    public List<Car> getCars() {
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
