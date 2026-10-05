package com.gianfrancomanca.service;

import com.gianfrancomanca.dao.CarDao;
import com.gianfrancomanca.dto.CarAvailable;
import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.enums.BookingStatus;
import com.gianfrancomanca.model.enums.Brand;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CarService {
    private CarDao carDao = new CarDao();
    private final CarBookingService carBookingService = CarBookingService.SingletonCarBookingService.getInstance();

    //get all cars
    public Car[] getCars() {
        return carDao.getCars();
    }

    //retrieve a car by id
    public Car getCarById(String id) {
        return carDao.getCarById(id).orElse(new Car( Brand.FORD, "Not Found","EF482LM", new BigDecimal("55.00"), false));
    }

    //get all cars availabilities
    public String[] getCarsAvailabilities() {
        String[] carIds  =retrieveCarIds();
        String[] availabilities = new String[carIds.length];
        for(int i=0; i<carIds.length; i++) {
            availabilities[i] = "("+ (i + 1) + ")" +createAvailabilities(carIds[i]);
        }

        return availabilities;
    }

    //Retrieve all unique car ids
    private String [] retrieveCarIds() {
        Car[] cars = getCars();
        String[] ids = new String[cars.length];
        for(int i=0; i<cars.length; i++) {
            ids[i] = cars[i].getId().toString();
        }
        return ids;
    }

    //Create a string with the car availability
    private String createAvailabilities(String carIds) {
        String checkedIds = "";
        String availability = "";
        String partialAvailability = "";
        CarBooking[] bookings = carBookingService.getAllBookings();

        Car car = getCarById(carIds);
        availability += "\n" + car.getBrand().getName() + " " + car.getName() + " ";
        for (CarBooking booking : bookings) {
            if(booking.getCarID().equals(carIds)) {
                if(!checkedIds.contains(booking.getCarID())) {
                    checkedIds  = booking.getCarID();
                }
                if(booking.getStatus() == BookingStatus.ACTIVE || booking.getStatus() == BookingStatus.BOOKED) {
                    partialAvailability +=  "\tfrom " + booking.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            + " to " + booking.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            + "\n";
                }
            }
        }
        if(partialAvailability.isEmpty()) {
            availability += "Currently available.\n";
        } else {
            availability += "Available except :\n" + partialAvailability;
        }

        return availability;
    }

    //get all electric cars availabilities
    public String[] getElectricCarsAvailabilities() {
        String[] eIds = retrieveECarsIds();
        String[] availabilities = new String[eIds.length];
        for(int i=0; i<eIds.length; i++) {
            availabilities[i] = "("+ (i + 1) + ")" +createAvailabilities(eIds[i]);
        }

        return availabilities;
    }

    //Retrieve all electric car ids
    private String[] retrieveECarsIds() {
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
