package com.gianfrancomanca.dao;

import com.gianfrancomanca.model.CarBooking;

/*Contains preloaded Users and Cars and methods to retrieve them*/
public class CarBookingDao {

    static int index = 0;

    private CarBooking[] bookings;

    public CarBookingDao() {
        bookings = new CarBooking[index];
    }

    //get all bookings
    public CarBooking[] getBookings() {
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
        for (CarBooking bkg : bookings) System.out.println(bkg);
    }

    //TODO: (not here) resolve BOOKED vs ACTIVE and  available cars based on booking dates


}
