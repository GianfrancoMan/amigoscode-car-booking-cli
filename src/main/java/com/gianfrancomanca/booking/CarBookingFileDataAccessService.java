package com.gianfrancomanca.booking;



import com.gianfrancomanca.util.FileManager;

import java.time.LocalDate;
import java.util.*;

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
        setStatus();
        FileManager.writeToFile(bookings, filePath);
        return (null == bookings) ? Collections.emptyList() : bookings;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        bookings.add(booking);
        setStatus();
        FileManager.writeToFile(bookings, filePath);
    }

    @Override
    public Optional<CarBooking> findBookingById(String id) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        setStatus();
        FileManager.writeToFile(bookings, filePath);
        for(CarBooking booking : bookings) {
            if(booking.getId().equals(id)) return Optional.of(booking);
        }
        return Optional.ofNullable(null);
    }

    @Override
    public boolean deleteBookingById(UUID id) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        for (CarBooking booking : bookings) {
            if (booking.getId().equals(id)
                    && booking.getStatus() != BookingStatus.ACTIVE
                    && booking.getStatus() != BookingStatus.COMPLETED
                    && booking.getStatus() != BookingStatus.CANCELED) {
                booking.setStatus(BookingStatus.CANCELED);
                FileManager.writeToFile(bookings, filePath);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CarBooking> getBookingsByUserId(String userId) {
        bookings = (List<CarBooking>) FileManager.readFromFile(filePath);
        setStatus();
        FileManager.writeToFile(bookings, filePath);
        List<CarBooking> userBookings = new ArrayList<>();
        for(CarBooking booking : bookings) {
            if (booking.getUserId().toString().equals(userId)) {
                userBookings.add(booking);
            }
        }
        return (null == userBookings) ? Collections.emptyList() : userBookings;
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
