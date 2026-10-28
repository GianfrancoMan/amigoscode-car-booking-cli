package com.gianfrancomanca.booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {

    List<CarBooking> getBookings();
    void saveBooking(CarBooking booking);
    Optional<CarBooking> findBookingById(String id);
    boolean deleteBookingById(UUID id);
    List<CarBooking> getBookingsByUserId(String userId);

}