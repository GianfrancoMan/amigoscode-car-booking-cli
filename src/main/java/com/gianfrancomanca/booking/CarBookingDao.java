package com.gianfrancomanca.booking;

import java.util.Optional;

public interface CarBookingDao {

    CarBooking[] getBookings();
    void addBooking(CarBooking booking);
    Optional<CarBooking> getBookingById(String id);
    boolean cancelBookingById(String id);
    CarBooking[] getBookingsByUserId(String userId);

}