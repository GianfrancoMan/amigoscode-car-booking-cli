package com.gianfrancomanca.view;

import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.User;
import com.gianfrancomanca.service.CarBookingService;
import com.gianfrancomanca.service.CarService;
import com.gianfrancomanca.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CLIService {
    static Scanner scanner = new Scanner(System.in);
    static CarBookingService carBookingService = CarBookingService.SingletonCarBookingService.getInstance();
    static UserService userService = new UserService();
    static CarService carService = new CarService();

    //main menu
    public static void userInterface() {
        int choice =0; boolean success = false;
        while (choice != 8 || success==false) {
            CLI.printHeader("\nPlease type a number between 1 and 7 for operations or 8 to exit:");
            CLI.mainMenu();
            CLI.printHeader("Type your choice...");
            if(scanner.hasNextInt()) {
                choice = CLIService.checkChoice(scanner, 1, 8);
                success = CLIService.manageChoice(choice);
            }else {
                scanner.nextLine();
            }
        }
        scanner.close();
        CLI.printHeader("Thank you for using our service, see you soon!\n");
    }


    //user choice to book a car
    private static boolean manageBookCarView(Scanner scanner) {

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate toDay = LocalDate.now();

        String userId = validateUserId(scanner);
        String username = "";
        User user= userService.getUserById(userId);
        username = user.getName();
        if(userId.equalsIgnoreCase("Cancel") || username.equalsIgnoreCase("not found")) {
            CLI.printError(userId.equalsIgnoreCase("Cancel") ? "Operation Cancelled" : "Unique identifier you provided is not valid, please try again");
            return false;
        }

        String[] serviceDates = validateServiceDates(scanner, toDay, username);
        if(serviceDates[0] == null || serviceDates[1] == null) {
            CLI.printError("ERROR!! operation cancelled cause the start date is after the end date or the start date is in the past");
            return false;
        }

        Car[] availableCars = carBookingService.getAvailableCars(serviceDates);
        if(availableCars.length == 0) {
            CLI.printHeader("No cars available for the selected period");
            return false;
        }

        String carId = validateChosenCar(scanner, availableCars);
        if(!carId.isEmpty()) {
            user = userService.getUserById(userId);
            CarBooking booking =
                    carBookingService.bookingCar(user.getId().toString(), carId, serviceDates[0], serviceDates[1], toDay.format(formater)).orElse(null);
            if(booking != null) {
                String[] elementsToPrint = createBookingElements(user, booking, carBookingService, formater);
                CLI.printBooking(elementsToPrint);
            }
            else {
                CLI.printError("ERROR!! Operation Failed");
                return false;
            }
        }

        return true;
    }

    //user choice to see all the bookings
    private static boolean manageAllBookingView() {
        CarBooking[] bookings = carBookingService.getAllBookings();
        String data[] = new String[bookings.length];
        if(null != bookings && bookings.length >= 0) {
            for(int i=0; i<bookings.length; i++){
                data[i] = "\n(" + (i+1) + ")\nBOOKING date: " + parseDateForView(bookings[i].getBookingDate()) + "\t\t id: " + bookings[i].getId().toString()
                                + "\nCUSTOMER\t\tname: " + userService.getUserById(bookings[i].getUserId()).getName() + "\t\tid: " + bookings[i].getUserId()
                                +"\nCAR\t\t model: "  + carService.getCarById(bookings[i].getCarID()).getBrand().getName() + " " + carService.getCarById(bookings[i].getCarID()).getName() + "\t\tregistration number: " + carService.getCarById(bookings[i].getCarID()).getRegistrationNumber()
                                +"\nBOOKING PERIOD\t\tstart: " + parseDateForView(bookings[i].getStartDate()) + "\t\tend:  " + parseDateForView(bookings[i].getEndDate())
                                +"\nSTATUS: " + bookings[i].getStatus().getStatusName();
            }
            CLI.printAllBookings(data);
            return true;
        }
        CLI.printError("Something was wrong, please try again later");
        return false;
    }

    //user choice to delete a booking
    private static boolean manageDeleteBookingView(Scanner scanner) {
        CLI.printHeader("Type the booking ID to delete it or \"Cancel\" to abort the operation:");
        if(scanner.hasNextLine()) scanner.nextLine();
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank()) {
            CLI.printError("Error! The Booking ID cannot be empty, please type the  booking ID or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        if(!userInput.equalsIgnoreCase("cancel"))  {
            boolean deleted = carBookingService.cancelBooking(userInput);
            if(deleted) {
                CLI.printHeader("Booking deleted successfully");
                return true;
            }
            CLI.printError("Operation failed!! possible causes: booking not exists or the booking is currently active or completed");
        }
        return true;
    }

    //user choice to see all booking of a cutomer
    private static boolean manageUsersBooking(Scanner scanner) {
        CLI.printHeader("Type the user ID to see its bookings or \"Cancel\" to abort the operation:");
        if(scanner.hasNextLine()) scanner.nextLine();
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank()) {
            CLI.printError("Error! The user ID cannot be empty, please type the  user ID or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        if(!userInput.equalsIgnoreCase("cancel"))  {
            CarBooking[] userBookings = carBookingService.getBookingsByUser(userInput);
            if(null != userBookings && userBookings.length != 0) {
                String customerName =
                        userService.getUserById(userInput).getName().equalsIgnoreCase("not found") ? "customer" : userService.getUserById(userInput).getName();
                CLI.printHeader("\nBookings made by "+ customerName + "...");
                var data = new String[userBookings.length];
                for(int i=0; i<userBookings.length; i++) {
                    data[i] = "\n(" + (i+1) + ")\nBOOKING date: " + parseDateForView(userBookings[i].getBookingDate()) + "\t\t id: " + userBookings[i].getId().toString()
                                    + "\nCAR\t\t model: " + carService.getCarById(userBookings[i].getCarID()).getBrand().getName() + " " + carService.getCarById(userBookings[i].getCarID()).getName() + "\t\tregistration number: " + carService.getCarById(userBookings[i].getCarID()).getRegistrationNumber()
                                    +"\nBOOKING PERIOD\t\tstart: " + parseDateForView(userBookings[i].getStartDate()) + "\t\tend:  " + parseDateForView(userBookings[i].getEndDate())
                                    +"\nSTATUS: " + userBookings[i].getStatus().getStatusName();
                }
                CLI.printAllBookings(data);
                return true;
            } else
                CLI.printHeader("Operation failed!! No bookings found for the user or the user does not exist\n");
        } else
            CLI.printHeader("Operation cancelled successfully\n");

        return true;
    }

    //user choice to see all the cars of the fleet
    public static boolean manageAllCarView() {
        CLI.printHeader(
            "\n--------------------------------------------------------------"
            + "\nAVAILABLE AND PARTIALLY AVAILABLE CARS ..."
            + "\n---------------------------------------------------------------\n");
        CLI.printArray(carService.getCarsAvailabilities());
        return  true;
    }

    private static boolean manageAllElectricCarView() {
        CLI.printHeader(
                "\n-----------------------------------------------------------------------"
                        + "\nAVAILABLE AND PARTIALLY AVAILABLE ELECTRIC CARS ..."
                        + "\n------------------------------------------------------------------------\n");
        CLI.printArray(carService.getElectricCarsAvailabilities());
        return true;
    }

    private static boolean manageAllUserView() {
        User[] users = userService.getAllUsers();
        String[] userData = new String[users.length];
        if(users.length > 0) {
            CLI.printHeader(
                    "\n-----------------------------------------------"
                            + "\nRENTAL CAR  -> CUSTOMER LIST\n"
                    + "----------------------------------------------\n"
            );
            CLI.printArray(createUserData(users, userData));
        }
        return true;
    }

    //check if the user typed a valid number
    public static int checkChoice(Scanner scanner, int min, int max) {
        int choice = scanner.nextInt();
        if (choice < min || choice > max) {
            return 0;
        }
        return choice;
    }

    //manage the user's choice from the main menu'
    public static boolean manageChoice(int choice) {
        return  switch (choice) {
            case 1 -> manageBookCarView(scanner);
            case 2 -> manageDeleteBookingView(scanner);
            case 3 -> manageUsersBooking(scanner);
            case 4 ->  manageAllBookingView();
            case 5 -> manageAllCarView();
            case 6 -> manageAllElectricCarView();
            case 7 -> manageAllUserView();
            case 8 -> true;
            default -> false;
        };
    }

    //manage the user's choice from the car menu
    public static String manageCarChoice(int choice, Car[] cars) {
        for (int i=0; i<cars.length; i++) {
            if(choice == (i+1)) return cars[i].getId().toString();
        }
        return "";
    }

    //create the user data array
    private static String[] createUserData(User [] users, String[] userData) {
        for(int i=0; i<users.length; i++) {
            userData[i] =String.format(" (%d)  Name: %-50s ID: %-50s", (i+1), users[i].getName(), users[i].getId().toString());
        }
        return userData;
    }

    //Ask for user name and validate the input
    private static String validateUserId(Scanner scanner) {
        if(scanner.hasNextLine()) scanner.nextLine();
        CLI.printHeader("\n        Rental Car      \nType customer ID or \"Cancel\" to cancel the operation: ");
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank() || userInput.length() < 3 ) {
            CLI.printError("Error! Name cannot be empty and should be at least of three characters, please type your name or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        return userInput;
    }

    //Ask for service dates and validate the input
    private static String[] validateServiceDates(Scanner dateScanner, LocalDate toDay, String username) {
        String[] dates = new String[2];
        String startDate, endDate ;

        CLI.printHeader("\n      Rental Car ->        " + username + "\nThe start date of the service... ");
        do {
            startDate = createDate(dateScanner);
        } while (startDate.isEmpty() || startDate.isBlank());

        CLI.printHeader("\n      Rental Car       " + "\nThe end date of the service... ");
        do {
            endDate = createDate(dateScanner);
        } while (endDate.isEmpty() || endDate.isBlank());

        LocalDate startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if(startLocalDate.isAfter(endLocalDate)  || toDay.isAfter(startLocalDate)) {
            return dates;
        }

        dates[0] = startDate;
        dates[1] = endDate;
        return dates;
    }

    //create a date from the user input
    private static String createDate(Scanner scanner) {
        String date = "";
        CLI.printSentence("type  the day of the month: ");
        String day = scanner.next() ;
        CLI.printSentence("\t\tthe month of the year: ");
        String month = scanner.next() ;
        CLI.printSentence("\t\tthe year:");
        String year = scanner.next() ;
        if(month.length() == 1) month = "0" + month;
        if(day.length() == 1) day = "0" + day;
        date += day + "-" + month + "-" + year;
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            CLI.printError("The date: "+ date +" is not valid, please type a valid date:");
            date = "";
        }
        return date;
    }

    private static String validateChosenCar(Scanner scanner, Car[] availableCars) {
        int max = availableCars.length + 1;
        int choice =0; String  carId = "";
        while (choice != max && carId.isEmpty()) {
            CLI.printHeader("\n\n           CARS AVAILABLE FOR THE SELECTED PERIOD   \n------------------------------------------------------------------");
            CLI.carMenu(availableCars);
            System.out.println("Type your choice...");
            if(scanner.hasNextInt()) {
                choice = CLIService.checkChoice(scanner, 1, max);
                carId = CLIService.manageCarChoice(choice, availableCars);
            }else {
                scanner.nextLine();
                choice = 0;
                CLI.printError("Typing Error!!\nPlease type a number between 1 and " + (max-1) + " for operations or " + max + " to abort the booking operation..");
            }
        }

        return carId;
    }

    private static String[] createBookingElements(User user, CarBooking booking, CarBookingService carBookingService, DateTimeFormatter formater) {
        String[] elementsToPrint = new String[6];
        elementsToPrint[0] = user.getId().toString();
        elementsToPrint[1] = user.getName();
        elementsToPrint[2] = carService.getCarById(booking.getCarID().toString()).getBrand().getName() + " " + carService.getCarById(booking.getCarID().toString()).getName()+ "\t\tregistration number: " + carService.getCarById(booking.getCarID().toString()).getRegistrationNumber();
        elementsToPrint[3] = booking.getStartDate().format(formater);
        elementsToPrint[4] = booking.getEndDate().format(formater);
        elementsToPrint[5] = booking.getRentalPrice().toString();
        return elementsToPrint;
    }

    private static String parseDateForView(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
