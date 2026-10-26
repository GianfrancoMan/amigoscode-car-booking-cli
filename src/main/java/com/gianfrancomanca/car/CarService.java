package com.gianfrancomanca.car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<Car> cars = getCars();
        List<String> ids = new ArrayList<>();
        for(int i=0; i<cars.size(); i++) {
            ids.add(cars.get(i).getId().toString());
        }
        return ids;
    }

    //Retrieve all electric car ids
    public List<String> retrieveECarsIds() {
        List<String> eIds = new ArrayList<>();
        for(int i=0; i<getCars().size(); i++) {
            if(getCars().get(i).isElectric()) {
                eIds.add(getCars().get(i).getId().toString());
            }
        }
        return eIds;}
}
