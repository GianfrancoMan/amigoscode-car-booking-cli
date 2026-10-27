package com.gianfrancomanca.car;

import java.math.BigDecimal;
import java.util.*;

public class CarArrayDataSourceService implements CarDao {
    public static List<Car> cars = new ArrayList<>();

    //Cars static initialization
    static {
        cars.add(new Car(UUID.fromString("16fe24a0-fac0-400e-8e9c-14c8d0c54808"), Brand.FORD, "Focus","EF482LM", new BigDecimal("55.00"), false));
        cars.add(new Car(UUID.fromString("92a99f79-8dff-4d9a-89c0-3c8b67667272"), Brand.AUDI, "Q4 e-tron","GA731NP", new BigDecimal("75.00"), true));
        cars.add(new Car(UUID.fromString("86c81e49-c649-4716-b4bb-a9d2afd9e68e"), Brand.MERCEDES, "Class C","HB294RT", new BigDecimal("110.00"), false));
        cars.add(new Car(UUID.fromString("2c4c4b47-3eac-41c5-93df-67258fc9b306"), Brand.CUPRA, "Formentor","GC615VS", new BigDecimal("120.00"), false));
        cars.add(new Car(UUID.fromString("ef0b263e-6ab5-49d8-92b1-bdf32117ff67"), Brand.FERRARI, "296 GTB","HD853XZ", new BigDecimal("2200.00"), false));
        cars.add(new Car(UUID.fromString("120d0a56-fe20-4398-b593-5e35b8f1a64c"), Brand.RENAULT, "Megane E-Tech Electric", "ZX 482 QL", new BigDecimal("105.00"), true));
    }

    //get all cars
    public List<Car> getCars() {
        return (null == cars) ? Collections.emptyList() : cars;
    }

    //retrieve a car by id
    public Optional<Car> getCarById(String id) {
        for(Car car : cars) {
            if(car.getId().toString().equals(id)) return Optional.of(car);
        }
        return Optional.empty();
    }
}
