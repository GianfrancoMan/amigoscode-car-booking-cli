package com.gianfrancomanca.car;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        List<String> makeAndModels = new ArrayList<>();
        for (int i = 0; i < 157; i++) makeAndModels.add(faker.vehicle().makeAndModel());
        var fakeCars = makeAndModels.stream().map( map -> {
            String make = map.substring(0, map.indexOf(" "));
            String model = map.substring(map.indexOf(" ")+ 1);
            return new FakeCar(make, model);
        });

        return fakeCars.filter( fcar ->
            fcar.make().equalsIgnoreCase(Brand.AUDI.getName())
                    || fcar.make().equalsIgnoreCase(Brand.FORD.getName())
                    || fcar.make().equalsIgnoreCase(Brand.RENAULT.getName()))
                .limit(10)
                .map(fc -> new Car
                        (UUID.randomUUID(),
                                brandExtraction(fc.make()),fc.model,
                                faker.vehicle().licensePlate(),
                                new BigDecimal(faker.commerce().price()),
                                faker.bool().bool()))
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    private Brand brandExtraction(String brand) {
        if(Brand.AUDI .getName().equalsIgnoreCase(brand)) return Brand.AUDI;
        else if(Brand.FORD.getName().equalsIgnoreCase(brand)) return Brand.FORD;
        else return Brand.RENAULT;
    }

    record FakeCar(String make, String model){}
}
