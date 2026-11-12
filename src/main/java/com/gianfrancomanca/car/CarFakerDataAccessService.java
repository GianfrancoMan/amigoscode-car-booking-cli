package com.gianfrancomanca.car;

import net.datafaker.Faker;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CarFakerDataAccessService implements CarDao{
    private List<Car> cars;
    public CarFakerDataAccessService() {
        cars = generateCars();
    }
    @Override
    public List<Car> getCars() {
        return cars;
    }

    @Override
    public Optional<Car> getCarById(String id) {
        return cars.stream().filter(car -> car.getId().toString().equals(id)).findFirst();
    }

    private List<Car> generateCars() {
        Faker faker =new Faker();
        var models = new ArrayList<String>();
        for (int i = 0; i < 10; i++)
           models.add(faker.vehicle().model()) ;

        return models.stream()
                .map(model ->
                    new Car(
                            UUID.randomUUID(),
                            Brand.values()[new Random().nextInt(Brand.values().length)],
                            model,
                            faker.vehicle().licensePlate(),
                            new BigDecimal(faker.commerce().price()),
                            !faker.bool().bool()))
                .collect(Collectors.toUnmodifiableList());
    }

}
