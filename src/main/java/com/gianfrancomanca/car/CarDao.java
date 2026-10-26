package com.gianfrancomanca.car;

import java.util.List;
import java.util.Optional;

public interface CarDao {


    //get all cars
    List<Car> getCars();

    //retrieve a car by id
    Optional<Car> getCarById(String id);
}
