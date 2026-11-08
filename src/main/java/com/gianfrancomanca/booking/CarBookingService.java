package com.gianfrancomanca.booking;

import com.gianfrancomanca.car.Car;
import com.gianfrancomanca.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarBookingService {

    private final CarBookingDao carBookingDao;
    private LocalDate toDay;

    public CarBookingService(CarBookingDao carBookingDao) {
        this.carBookingDao = carBookingDao;
        toDay = LocalDate.now();
    }

    public Optional<CarBooking> bookingCar(User user, Car car, LocalDate startDate, LocalDate endDate) {
        CarBooking booking= null;
        if(null == car || null == user) return Optional.empty();
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal rentalPrice = car.getRentalPrice().multiply(new BigDecimal(days));
        booking = new CarBooking(user.getId().toString(), car.getId().toString(), toDay, startDate, endDate, rentalPrice);
        carBookingDao.saveBooking(booking);
        return Optional.of(booking);
    }

    //Get all cars that are not booked during the given dates
    public List<Car> getAvailableCars(LocalDate[] dates, List<Car> cars) {
        List<CarBooking> bookings = carBookingDao.getBookings();
        List<Car> availableCars = cars.stream().filter(
                        car -> bookings.stream().noneMatch(booking -> booking.getCarID().equals(car.getId().toString()) &&
                                isDateUnavailable(dates[0], dates[1], booking)))
                .collect(Collectors.toList());
        return availableCars;
    }

    //get all bookings
    public List<CarBooking> getAllBookings() {
        return carBookingDao.getBookings();
    }

    //get a booking by its id
    public CarBooking getBookingById(String id) {
        return carBookingDao.findBookingById(id).orElse(null);
    }

    //cancel a booking
    public boolean cancelBooking(String id) {
        try {
            return carBookingDao.deleteBookingById(UUID.fromString(id));
        } catch (Exception e) {
            return false;
        }
    }

    //check if the given dates are unavailable based on the booking requirement
    private boolean isDateUnavailable(LocalDate startDate, LocalDate endDate, CarBooking booking) {
        if(booking.getStatus() == BookingStatus.CANCELED) {
            return false;
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate) +1;
        //in this loop there are no collections involved, so no changes needed for it.
        for(int i=0; i<days; i++) {
            LocalDate dateToCheck = startDate.plusDays(i);
            if((booking.getStartDate().isEqual(dateToCheck) || booking.getStartDate().isBefore(dateToCheck)) &&
                    (booking.getEndDate().isEqual(dateToCheck) || booking.getEndDate().isAfter(dateToCheck))) {
                return true;
            }
        }
        return false;
    }

    //Asks CarBookingDao for booking by user id
    public List<CarBooking> getBookingsByUser(String userId) {
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







