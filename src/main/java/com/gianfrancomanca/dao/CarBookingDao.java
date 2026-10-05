package com.gianfrancomanca.dao;

import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.enums.BookingStatus;

import java.time.LocalDate;
import java.util.Optional;

/*Contains preloaded Users and Cars and methods to retrieve them*/
public class CarBookingDao {

    static int index = 0;

    //The where bookings are stored
    private CarBooking[] bookings;

    public CarBookingDao() {
        bookings = new CarBooking[index];
    }

    //get all bookings
    public CarBooking[] getBookings() {
        setStatus();
        return bookings;
    }

    //add a new booking
    public void addBooking(CarBooking booking) {
        index++;
        CarBooking[] bookingsHelper = new CarBooking[index];
        System.out.println("carBookings.length: " + bookingsHelper.length);
        if(bookingsHelper.length == 1) bookingsHelper[0] = booking;
        else {
            for (int i = 0; i < bookings.length; i++) {
                bookingsHelper[i] = bookings[i];
            }
            bookingsHelper[index-1] = booking;
        }
        bookings = bookingsHelper;
        for (CarBooking bkg : bookings) System.out.println(bkg);//to delete later
        setStatus();
    }

    //get a booking by its id
    public Optional<CarBooking> getBookingById(String id) {
        setStatus();
        for(CarBooking booking : bookings) {
            if(booking.getId().toString().equals(id)) return Optional.of(booking);
        }
        return Optional.ofNullable(null);
    }

    //cancel a booking based on its id and current status
    public boolean cancelBookingById(String id) {
        for (CarBooking booking : bookings) {
            if (booking.getId().toString().equals(id) && booking.getStatus() != BookingStatus.ACTIVE && booking.getStatus() != BookingStatus.COMPLETED) {
                booking.setStatus(BookingStatus.CANCELLED);
                return true;
            }
        }
        return false;
    }

    //get all bookings of a specific user
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

    //get all bookings of a specific status
    public String getCarIdsByStatus(BookingStatus ...inputStatus) {
        setStatus();
        String ids = "";
        for(CarBooking booking : bookings) {
            for(BookingStatus wantedStatus : inputStatus) {
                if(booking.getStatus() == wantedStatus)
                    ids = booking.getCarID() + ",";
            }
        }
        if(ids.length() > 0) ids = ids.substring(0, ids.length()-1);

        return ids;
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


}
