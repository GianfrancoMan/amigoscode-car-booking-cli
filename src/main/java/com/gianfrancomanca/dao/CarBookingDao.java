package com.gianfrancomanca.dao;

import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;

/*Contains preloaded Users and Cars and methods to retrieve them*/
public class CarBookingDao {

    public static Car[] cars = new Car[5];
    private CarBooking[] bookings = new CarBooking[100];
    //get all bookings
    public CarBooking[] getBookings() {
        int indexFilled = 0;
        for (int i = 0; i < bookings.length; i++) {
            if(bookings[i] == null) {
                indexFilled = i;
                break;
            }
        }
        CarBooking[] bookingsAvailable = new CarBooking[indexFilled];
        for (int i = 0; i < bookingsAvailable.length; i++) {
            bookingsAvailable[i] = bookings[i];
        }
        return bookingsAvailable;
    }

    //add a new booking
    public void addBooking(CarBooking booking) {
        for(int i=0; i<bookings.length; i++) {
            if(null == bookings[i]) {
                bookings[i] = booking;
                break;
            }
        }
        System.out.println("prenotazione registrate:");
        for (CarBooking bkg : bookings) System.out.println(bkg);
    }


}
