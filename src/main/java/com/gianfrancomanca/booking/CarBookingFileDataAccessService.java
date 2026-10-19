package com.gianfrancomanca.booking;



import com.gianfrancomanca.util.FileManager;

import java.time.LocalDate;
import java.util.Optional;

public class CarBookingFileDataAccessService implements CarBookingDao{
    private  int index;
    private  CarBooking[] bookings;
    private final String filePath;

    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
        this.initializeBookings();
        index = bookings.length;
    }

    @Override
    public CarBooking[] getBookings() {
        bookings = (CarBooking[]) FileManager.readFromFile(filePath);
        setStatus();
        FileManager.writeToFile(bookings, filePath);
        return bookings;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        bookings = (CarBooking[]) FileManager.readFromFile(filePath);
        index = bookings.length + 1;
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
        FileManager.writeToFile(bookings, filePath);
    }

    @Override
    public Optional<CarBooking> findBookingById(String id) {
        bookings = (CarBooking[]) FileManager.readFromFile(filePath);
        setStatus();
        FileManager.writeToFile(bookings, filePath);
        for(CarBooking booking : bookings) {
            if(booking.getId().toString().equals(id)) return Optional.of(booking);
        }
        return Optional.ofNullable(null);
    }

    @Override
    public boolean deleteBookingById(String id) {
        bookings = (CarBooking[]) FileManager.readFromFile(filePath);
        for (CarBooking booking : bookings) {
            if (booking.getId().toString().equals(id) && booking.getStatus() != BookingStatus.ACTIVE && booking.getStatus() != BookingStatus.COMPLETED) {
                booking.setStatus(BookingStatus.CANCELLED);
                FileManager.writeToFile(bookings, filePath);
                return true;
            }
        }
        return false;
    }

    @Override
    public CarBooking[] getBookingsByUserId(String userId) {
        bookings = (CarBooking[]) FileManager.readFromFile(filePath);
        setStatus();
        FileManager.writeToFile(bookings, filePath);
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
            if (booking.getStatus() != BookingStatus.CANCELLED) {
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
        bookings = (CarBooking[]) FileManager.readFromFile(filePath);
        if(null == bookings) {
            bookings = new CarBooking[0];
            FileManager.writeToFile(bookings, filePath);
        }
    }
}
