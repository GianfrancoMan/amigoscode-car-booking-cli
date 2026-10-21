package com.gianfrancomanca.booking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CarBookingArrayDataAccessService  implements CarBookingDao {

    static int index = 0;

    //The where bookings are stored
    private CarBooking[] bookings;

    public CarBookingArrayDataAccessService() {
        bookings = new CarBooking[index];
    }

    @Override
    public CarBooking[] getBookings() {
        setStatus();
        return bookings;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        index++;
        CarBooking[] bookingsHelper = new CarBooking[index];
        if(bookingsHelper.length == 1) bookingsHelper[0] = booking;
        else {
            for (int i = 0; i < bookings.length; i++) {
                bookingsHelper[i] = bookings[i];
            }
            bookingsHelper[index-1] = booking;
        }
        bookings = bookingsHelper;
        setStatus();
    }

    @Override
    public Optional<CarBooking> findBookingById(String id) {
        setStatus();
        for(CarBooking booking : bookings) {
            if(booking.getId().equals(id)) return Optional.of(booking);
        }
        return Optional.ofNullable(null);
    }

    @Override
    public boolean deleteBookingById(UUID id) {
        for (CarBooking booking : bookings) {
            if (booking.getId().equals(id)
                    && booking.getStatus() != BookingStatus.ACTIVE
                    && booking.getStatus() != BookingStatus.COMPLETED
                    && booking.getStatus() != BookingStatus.CANCELED) {
                booking.setStatus(BookingStatus.CANCELED);
                return true;
            }
        }
        return false;
    }

    @Override
    public CarBooking[] getBookingsByUserId(String userId) {
        setStatus();
        CarBooking[] userBookings;
        int bookingLength = 0;

        for(CarBooking booking : bookings)
            if(booking.getUserId().toString().equals(userId))
                bookingLength++;

        userBookings = new CarBooking[bookingLength];
        int userBookingsIndex = 0;
        if(userBookings.length > 0) {
            for(CarBooking booking : bookings) {
                if (booking.getUserId().toString().equals(userId)) {
                    userBookings[userBookingsIndex] = booking;
                    userBookingsIndex++;
                }
            }
        }
        return userBookings;
    }

    //set the status of the bookings
    private void setStatus() {
        for (CarBooking booking : bookings) {
            LocalDate today = LocalDate.now();
            if (booking.getStatus() != BookingStatus.CANCELED) {
                if ((booking.getStartDate().isEqual(today) || booking.getEndDate().isEqual(today)) ||
                        (booking.getStartDate().isBefore(today) && booking.getEndDate().isAfter(today))) {
                    booking.setStatus(BookingStatus.ACTIVE);
                } else if (today.isAfter(booking.getEndDate())) {
                    booking.setStatus(BookingStatus.COMPLETED);
                } else  booking.setStatus(BookingStatus.BOOKED);
            }
        }
    }

}//end








