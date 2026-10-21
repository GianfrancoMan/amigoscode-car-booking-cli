package com.gianfrancomanca.booking;

import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {

    CarBooking[] getBookings();
    void saveBooking(CarBooking booking);
    Optional<CarBooking> findBookingById(String id);
    boolean deleteBookingById(UUID id);
    CarBooking[] getBookingsByUserId(String userId);

}