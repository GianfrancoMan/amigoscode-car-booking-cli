package com.gianfrancomanca.booking;

import com.gianfrancomanca.car.Car;
import com.gianfrancomanca.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CarBookingService {

    private final CarBookingDao carBookingDao = new CarBookingDao();
    private LocalDate toDay;

    public CarBookingService() {
        toDay = LocalDate.now();
    }

    public Optional<CarBooking> bookingCar(User user, Car car, LocalDate startDate, LocalDate endDate) {
        CarBooking booking= null;
        if(null == car || null == user) return Optional.empty();
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal rentalPrice = car.getRentalPrice().multiply(new BigDecimal(days));
        booking = new CarBooking(user.getId().toString(), car.getId().toString(), toDay, startDate, endDate, rentalPrice);
        carBookingDao.addBooking(booking);
        return Optional.of(booking);
    }

    //Get all cars that are not booked during the given dates
    public Car[] getAvailableCars(LocalDate[] dates, Car[] cars) {
        CarBooking[] bookings = carBookingDao.getBookings();
        StringBuilder carIdsString = new StringBuilder();
        for(int i=0; i<bookings.length; i++) {
            if(isDateUnavailable(dates[0], dates[1], bookings[i])) {
                if(!carIdsString.toString().contains(bookings[i].getCarID()))
                    carIdsString.append(bookings[i].getCarID()).append(",");
            }
        }
        if(!carIdsString.isEmpty()) {
            carIdsString = new StringBuilder(carIdsString.substring(0, carIdsString.length() - 1));
            String[] carIds = carIdsString.toString().split(",");
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
        return cars;
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
        long days = ChronoUnit.DAYS.between(startDate, endDate) +1;
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
        return carBookingDao.getBookingsByUserId(userId);
    }

    public Optional<LocalDate[]> validateInputDates(DateTimeFormatter formater, String startDate, String endDate) {
        LocalDate startLocalDate;
        LocalDate endLocalDate;
        LocalDate[] dates = new LocalDate[2];
        try {
            startLocalDate = LocalDate.parse(startDate, formater);
            endLocalDate = LocalDate.parse(endDate, formater);

        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
        if(startLocalDate.isAfter(endLocalDate)  || toDay.isAfter(startLocalDate)) {
            return Optional.empty();
        }
        dates[0] = startLocalDate;
        dates[1] = endLocalDate;
        return Optional.of(dates);
    }
}







