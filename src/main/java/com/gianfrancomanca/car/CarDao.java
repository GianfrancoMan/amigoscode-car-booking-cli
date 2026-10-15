package com.gianfrancomanca.car;

import java.util.Optional;

public interface CarDao {


    //get all cars
    Car[] getCars();

    //retrieve a car by id
    Optional<Car> getCarById(String id);
}
