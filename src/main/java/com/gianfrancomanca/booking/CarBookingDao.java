package com.gianfrancomanca.booking;

import java.util.Optional;

public interface CarBookingDao {

    CarBooking[] getBookings();
    void saveBooking(CarBooking booking);
    Optional<CarBooking> findBookingById(String id);
    boolean deleteBookingById(String id);
    CarBooking[] getBookingsByUserId(String userId);

}