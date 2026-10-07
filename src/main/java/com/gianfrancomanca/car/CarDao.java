package com.gianfrancomanca.car;

import java.math.BigDecimal;
import java.util.Optional;

public class CarDao {
    public static Car[] cars = new Car[6];

    //Cars static initialization
    static {
        cars[0] = new Car( Brand.FORD, "Focus","EF482LM", new BigDecimal("55.00"), false);
        cars[1] = new Car(Brand.AUDI, "Q4 e-tron","GA731NP", new BigDecimal("75.00"), true);
        cars[2] = new Car(Brand.MERCEDES, "Class C","HB294RT", new BigDecimal("110.00"), false);
        cars[3] = new Car(Brand.CUPRA, "Formentor","GC615VS", new BigDecimal("120.00"), false);
        cars[4] = new Car(Brand.FERRARI, "296 GTB","HD853XZ", new BigDecimal("2200.00"), false);
        cars[5] = new Car(Brand.RENAULT, "Megane E-Tech Electric", "ZX 482 QL", new BigDecimal("105.00"), true);
    }


    //get all cars
    public Car[] getCars() {
        return cars;
    }

    //retrieve a car by id
    public Optional<Car> getCarById(String id) {
        for(Car car : cars) {
            if(car.getId().toString().equals(id)) return Optional.of(car);
        }
        return Optional.ofNullable(null);
    }
}
