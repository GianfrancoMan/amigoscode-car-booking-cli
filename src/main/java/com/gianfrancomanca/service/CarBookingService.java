package com.gianfrancomanca.service;

import com.gianfrancomanca.dao.CarBookingDao;
import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.User;
import com.gianfrancomanca.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

public class CarBookingService {

    public CarBookingService() {}
    CarBookingDao carBookingDao = new CarBookingDao();


    public Car[] getAllCars() {
        return carBookingDao.getCars();
    }


    public Optional<CarBooking> bookCar(String userId, String carId, String startDate, String endDate, String toDay) {
        CarBooking booking= null;
        Car car = carBookingDao.getCarById(carId).orElse(null);
        if(car == null) return Optional.ofNullable(booking);
        LocalDate localStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate localEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate localToDay = LocalDate.parse(toDay, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int days = Period.between(localStartDate, localEndDate).getDays() + 1;
        BigDecimal rentalPrice = car.getRentalPrice().multiply(new BigDecimal(days));
        booking = new CarBooking(
                userId,
                carId,
                localToDay,
                localStartDate,
                localEndDate,
                rentalPrice,
                localStartDate.equals(toDay) ? BookingStatus.ACTIVE : BookingStatus.BOOKED);
        carBookingDao.addBooking(booking);
        return Optional.of(booking);
    }

    //Get all cars that are not booked during the given dates
    public Car[] getAvailableCars(String[] dates) {
        LocalDate startDate = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        CarBooking[] bookings = carBookingDao.getBookings();
        StringBuilder carIdsString = new StringBuilder();
        for(int i=0; i<bookings.length; i++) {
            if((bookings[i].getStartDate().isEqual(startDate) || bookings[i].getEndDate().isEqual(startDate)) || ((bookings[i].getStartDate().isBefore(startDate) && bookings[i].getEndDate().isAfter(startDate)))) {
                if(!carIdsString.toString().contains(bookings[i].getCarID()))
                    carIdsString.append(bookings[i].getCarID()).append(",");
            }
        }

        if(!carIdsString.isEmpty()) {
            carIdsString = new StringBuilder(carIdsString.substring(0, carIdsString.length() - 1));
            String[] carIds = carIdsString.toString().split(",");
            Car[] cars = carBookingDao.getCars();
            Car[] availableCars = new Car[cars.length - carIds.length];
            int availableIndex = 0;
            for(int i=0; i<cars.length; i++) {
                int indexFound = -1;
                for(int index=0; index<carIds.length; index++) {
                    if(cars[i].getId().toString().equals(carIds[index])) {
                        indexFound = index;
                    }
                }
                if (indexFound == -1) {
                    availableCars[availableIndex] = cars[i];
                    availableIndex++;
                }
            }
                return availableCars;
        }
        return carBookingDao.getCars();
    }

    //add a new user
    public void addUser(User user) {
        carBookingDao.addUser(user);
    }

    //get user based on id
    public User getUserById(String id) {
        return carBookingDao.getUserById(id).orElse(null);
    }

    //get car based on id
    public Car getCarById(String id) {
        return carBookingDao.getCarById(id).orElse(null);
    }
}
