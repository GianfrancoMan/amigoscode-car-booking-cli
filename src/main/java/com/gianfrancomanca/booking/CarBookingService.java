package com.gianfrancomanca.booking;

import com.gianfrancomanca.car.CarService;
import com.gianfrancomanca.car.Car;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CarBookingService {

    CarBookingDao carBookingDao = new CarBookingDao();
    CarService carService = new CarService();

    private CarBookingService() {}
    public static class SingletonCarBookingService {
        private static final CarBookingService INSTANCE = new CarBookingService();

        public static CarBookingService getInstance() {
            return INSTANCE;
        }
    }
    public Optional<CarBooking> bookingCar(String userId, String carId, String startDate, String endDate, String toDay) {
        CarBooking booking= null;
        Car car = carService.getCarById(carId);
        if(car.getName().equalsIgnoreCase("not found")) return Optional.ofNullable(booking);
        LocalDate localStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate localEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate localToDay = LocalDate.parse(toDay, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        long days = ChronoUnit.DAYS.between(localStartDate, localEndDate) + 1;
        BigDecimal rentalPrice = car.getRentalPrice().multiply(new BigDecimal(days));
        booking = new CarBooking(userId, carId, localToDay, localStartDate, localEndDate, rentalPrice);
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
            if(isDateUnavailable(startDate, endDate, bookings[i])) {
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

    //get all bookings
    public CarBooking[] getAllBookings() {
        return carBookingDao.getBookings();
    }

    //get a booking by its id
    public CarBooking getBookingById(String id) {
        return carBookingDao.getBookingById(id).orElse(null);
    }

    //cancel a booking
    public boolean cancelBooking(String id) {
        return carBookingDao.cancelBookingById(id);
    }

    //check if the given dates are unavailable based on the booking requirement
    private boolean isDateUnavailable(LocalDate startDate, LocalDate endDate, CarBooking booking) {
        if(booking.getStatus() == BookingStatus.CANCELLED) {
            return false;
        }
        int days = Period.between(startDate, endDate).getDays() +1;
        for(int i=0; i<days; i++) {
            LocalDate dateToCheck = startDate.plusDays(i);
            if((booking.getStartDate().isEqual(dateToCheck) || booking.getEndDate().isEqual(dateToCheck)) ||
                    (booking.getStartDate().isBefore(dateToCheck) && booking.getEndDate().isAfter(dateToCheck))) {
                return true;
            }
        }
        return false;
    }

    //Asks CarBookingDao for booking by user id
    public CarBooking[] getBookingsByUser(String userId) {
        return carBookingDao.getBookingsByUserId(userId).length > 0 ? carBookingDao.getBookingsByUserId(userId) : null;
    }
}







