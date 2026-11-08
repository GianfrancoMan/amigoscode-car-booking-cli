package com.gianfrancomanca.car;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    //get all cars
    public List<Car> getCars() {
        return carDao.getCars();
    }

    //retrieve a car by id
    public Optional<Car> getCarById(String id) {
        return carDao.getCarById(id);
    }

    //Retrieve all unique car ids
    public List<String> retrieveCarIds() {
        return getCars().stream().map(car -> car.getId().toString())
                .collect(Collectors.toList());
    }

    //Retrieve all electric car ids
    public List<String> retrieveECarsIds() {
        return getCars().stream().filter(car -> car.isElectric())
                .map(car -> car.getId().toString())
                    .collect(Collectors.toList());
        }
}
