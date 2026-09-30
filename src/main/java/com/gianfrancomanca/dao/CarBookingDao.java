package com.gianfrancomanca.dao;

import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.User;
import com.gianfrancomanca.model.enums.Brand;

import java.math.BigDecimal;
import java.util.Optional;

/*Contains preloaded Users and Cars and methods to retrieve them*/
public class CarBookingDao {

    private User[] users = new User[100];
    public static Car[] cars = new Car[5];
    private CarBooking[] bookings = new CarBooking[100];

    //Cars static initialization
    static {
        cars[0] = new Car( Brand.FORD, "Focus","EF482LM", new BigDecimal("55.00"), false);
        cars[1] = new Car(Brand.AUDI, "Q4 e-tron","GA731NP", new BigDecimal("75.00"), true);
        cars[2] = new Car(Brand.MERCEDES, "Class C","HB294RT", new BigDecimal("110.00"), false);
        cars[3] = new Car(Brand.CUPRA, "Formentor","GC615VS", new BigDecimal("120.00"), false);
        cars[4] = new Car(Brand.FERRARI, "296 GTB","HD853XZ", new BigDecimal("2200.00"), false);
    }

    //get all users, cars and bookings
    public User[] getUsers() {
        int indexFilled = 0;
        for (int i = 0; i < users.length; i++) {
            if(users[i] == null) {
                indexFilled = i+1;
                break;
            }
        }
        User[] usersAvailable = new User[indexFilled];
        for (int i = 0; i < usersAvailable.length; i++) {
            usersAvailable[i] = users[i];
        }
        return usersAvailable;
    }
    //get all cars
    public Car[] getCars() {
        return cars;
    }
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
    //add a new user
    public void addUser(User user) {
        for(int i=0; i<users.length; i++) {
            if(users[i] == null) {
                users[i] = user;
                break;
            }
        }
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

    //retrieve a car by id
    public Optional<Car> getCarById(String id) {
        for(Car car : cars) {
            if(car.getId().toString().equals(id)) return Optional.ofNullable(car);
        }
        return Optional.ofNullable(null);
    }

    //retrieve a user by id
    public Optional<User> getUserById(String id) {
        for(User user : users) {
            if(user.getId().equals(id)) return Optional.ofNullable(user);
        }
        return Optional.ofNullable(null);
    }


}
