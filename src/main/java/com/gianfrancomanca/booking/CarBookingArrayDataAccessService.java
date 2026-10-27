package com.gianfrancomanca.booking;

import java.time.LocalDate;
import java.util.*;

public class CarBookingArrayDataAccessService  implements CarBookingDao {

    static int index = 0;

    //The where bookings are stored
    private List<CarBooking> bookings;

    public CarBookingArrayDataAccessService() {
        bookings = new ArrayList<>();
    }

    @Override
    public List<CarBooking> getBookings() {
        setStatus();
        return (bookings ==null)  ? Collections.emptyList() : bookings;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        bookings.add(booking);
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
    public List<CarBooking> getBookingsByUserId(String userId) {
        setStatus();
        List<CarBooking> userBookings;

        userBookings = new ArrayList<>();
        if(!userBookings.isEmpty()) {
            for(CarBooking booking : bookings) {
                if (booking.getUserId().toString().equals(userId)) {
                    userBookings.add(booking);
                }
            }
        }
        return (userBookings == null) ? Collections.emptyList() : userBookings;
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








