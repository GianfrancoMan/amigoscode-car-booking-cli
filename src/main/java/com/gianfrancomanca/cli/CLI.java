package com.gianfrancomanca.cli;

import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.service.CarBookingService;

public class CLI {

    //print the main menu
    public static void mainMenu() {
        System.out.println("| ---------------------------------------|");
        System.out.println("          Welcome to Car Rental!          ");
        System.out.println("-----------------------------------------");
        System.out.println("|   1 to book a car                            |");
        System.out.println("|   2 to delete a booking                 |");
        System.out.println("|   3 to all view users booked class |");
        System.out.println("|   4 to view all bookings                 |");
        System.out.println("|   5 to view available cars              |");
        System.out.println("|   6 to view available eletric cars   |");
        System.out.println("|   7 to view all users                       |");
        System.out.println("|   8 to exit                                      |");
        System.out.println("| ------------------ ------------------- |");
    }

    //print the Car Menu
    public static void carMenu(Car[] cars) {
        System.out.println("\n\n");
        System.out.println("           CARS AVAILABLE FOR THE SELECTED PERIOD   ");
        System.out.println("------------------------------------------------------------------");
        int i = 1;
        for (Car car : cars) {
            System.out.println(
                    "    Type " + i + " to choose " + car.getBrand().getName()
                            + " " + car.getName() + " "
                            + (car.isElectric() ? " Electric: Yes " : " Electric: No ")
                            + "Price: " + car.getRentalPrice() + "€ (per day)"
            );
            ++i;
        }            System.out.println("    Type " + (cars.length +1)+ " to Cancel");
        System.out.println("---------------------------------------------------------------");
    }

    public static void printBooking(String[] data) {

        System.out.println("                Rental Car Booking Details:");
        System.out.println("-----------------------------------------------------------");
        System.out.println("\t\tCustomer ID: " + data[0]);
        System.out.println("\t\tCustomer Name: " + data[1]);
        System.out.println("\t\tCar model: " + data[2]);
        System.out.println("\t\tStart date: " + data[3]);
        System.out.println("\t\tEnd date: " + data[4]);
        System.out.println("\t\tRental price: " + data[5] + "€") ;
        System.out.println();
        System.out.println("\t\tOperation completed successfully.");
        System.out.println("-----------------------------------------------------------\n");
    }
}
