package com.gianfrancomanca.booking;



import com.gianfrancomanca.util.FileManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarBookingFileDataAccessService implements CarBookingDao{
    private  List<CarBooking> bookings;
    private final String filePath;

    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
        this.initializeBookings();
    }

    @Override
    public List<CarBooking> getBookings() {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        if(null != bookings)  bookings = setStatus();
        return (null == bookings) ? Collections.emptyList() : bookings;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        bookings.add(booking);
        bookings = setStatus();
    }

    @Override
    public Optional<CarBooking> findBookingById(String id) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        if(null != bookings) bookings = setStatus();
        return bookings.stream().filter(booking -> booking.getId().equals(id)).findFirst()
                .map(booking -> Optional.of(booking).orElse(null));
    }

    @Override
    public boolean deleteBookingById(UUID id) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        var bookingId = bookings.stream().filter(booking -> booking.getId().equals(id)).findFirst().orElse(null);
        if (null != bookingId && bookingId.getStatus() != BookingStatus.ACTIVE
                && bookingId.getStatus() != BookingStatus.COMPLETED && bookingId.getStatus() != BookingStatus.CANCELED) {
            bookingId.setStatus(BookingStatus.CANCELED);
            FileManager.writeToFile(bookings, filePath);
            return true;
        }
        return false;
    }

    @Override
    public List<CarBooking> getBookingsByUserId(String userId) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        if(null != bookings) bookings = setStatus();

        return bookings.stream().filter(booking -> booking.getUserId().toString().equals(userId))
                .collect(Collectors.toList());
    }

    //set the status of the bookings
    private List<CarBooking> setStatus() {
        List<CarBooking> peekBookings = bookings.stream().peek(
                booking -> {
                    LocalDate today = LocalDate.now();
                    if (booking.getStatus() != BookingStatus.CANCELED && booking.getStatus() != BookingStatus.COMPLETED) {
                        if ((booking.getStartDate().isEqual(today) || booking.getEndDate().isEqual(today)) ||
                                (booking.getStartDate().isBefore(today) && booking.getEndDate().isAfter(today))) {
                            booking.setStatus(BookingStatus.ACTIVE);
                        } else if (today.isAfter(booking.getEndDate())) {
                            booking.setStatus(BookingStatus.COMPLETED);
                        } else booking.setStatus(BookingStatus.BOOKED);
                    }
                }
        ).collect(Collectors.toList());
        FileManager.writeToFile(peekBookings, filePath);
        return peekBookings;
    }

    /*
     * Initialize the bookings array if it is null and write it to the file.
     */
    private void initializeBookings() {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        if(null == bookings) {
            bookings = new ArrayList<>();
            FileManager.writeToFile(bookings, filePath);
        }
    }
}
