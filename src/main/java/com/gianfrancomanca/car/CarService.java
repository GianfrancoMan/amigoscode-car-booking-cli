package com.gianfrancomanca.car;

import java.util.Optional;

public class CarService {
    private final CarDao carDao = new CarDao();

    //get all cars
    public Car[] getCars() {
        return carDao.getCars();
    }

    //retrieve a car by id
    public Optional<Car> getCarById(String id) {
        return carDao.getCarById(id);
    }

    //Retrieve all unique car ids
    public String [] retrieveCarIds() {
        Car[] cars = getCars();
        String[] ids = new String[cars.length];
        for(int i=0; i<cars.length; i++) {
            ids[i] = cars[i].getId().toString();
        }
        return ids;
    }

    //Retrieve all electric car ids
    public String[] retrieveECarsIds() {
        int ie = 0;
        for(int i=0; i<getCars().length; i++) {
            if(getCars()[i].isElectric()) ie++;
        }

        String[] eIds = new String[ie];
        int eIndex = 0;
        for(int i=0; i<getCars().length; i++) {
            if(getCars()[i].isElectric()) {
                eIds[eIndex++] = getCars()[i].getId().toString();
            }
        }
        return eIds;}
}
