package com.gianfrancomanca.car;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface CarDao {


    //get all cars
    Car[] getCars();

    //retrieve a car by id
    Optional<Car> getCarById(String id);
}
