package com.gianfrancomanca.service;

import com.gianfrancomanca.dao.CarBookingDao;
import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CarBookingService {

    public CarBookingService() {}
    CarBookingDao carBookingDao = new CarBookingDao();
    CarService carService = new CarService();


    public Optional<CarBooking> bookCar(String userId, String carId, String startDate, String endDate, String toDay) {
        CarBooking booking= null;
        Car car = carService.getCarById(carId);
        if(car.getName().equalsIgnoreCase("not found")) return Optional.ofNullable(booking);
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
        LocalDate endDate = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        CarBooking[] bookings = carBookingDao.getBookings();
        StringBuilder carIdsString = new StringBuilder();
        for(int i=0; i<bookings.length; i++) {
            if((bookings[i].getStartDate().isEqual(startDate) || bookings[i].getEndDate().isEqual(startDate)) ||
                    ((bookings[i].getStartDate().isBefore(startDate) && bookings[i].getEndDate().isAfter(startDate))) ||
                        (bookings[i].getStartDate().isEqual(endDate) || bookings[i].getEndDate().isEqual(endDate)) ||
                            ((bookings[i].getStartDate().isBefore(endDate) && bookings[i].getEndDate().isAfter(endDate)))
            ) {
                if(!carIdsString.toString().contains(bookings[i].getCarID()))
                    carIdsString.append(bookings[i].getCarID()).append(",");
            }
        }

        if(!carIdsString.isEmpty()) {
            carIdsString = new StringBuilder(carIdsString.substring(0, carIdsString.length() - 1));
            String[] carIds = carIdsString.toString().split(",");
            Car[] cars = carService.getCars();
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
        return carService.getCars();
    }
}
