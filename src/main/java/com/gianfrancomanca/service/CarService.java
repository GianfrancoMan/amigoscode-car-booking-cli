package com.gianfrancomanca.service;

import com.gianfrancomanca.dao.CarDao;
import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.enums.Brand;

import java.math.BigDecimal;
import java.util.Optional;

public class CarService {
    private CarDao carDao = new CarDao();

    //get all cars
    public Car[] getCars() {
        return carDao.getCars();
    }

    //retrieve a car by id
    public Car getCarById(String id) {
        return carDao.getCarById(id).orElse(new Car( Brand.FORD, "Not Found","EF482LM", new BigDecimal("55.00"), false));
    }
}
