package com.gianfrancomanca.cli;

import com.gianfrancomanca.model.Car;
import com.gianfrancomanca.model.CarBooking;
import com.gianfrancomanca.model.User;
import com.gianfrancomanca.service.CarBookingService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CLIService {
    static Scanner scanner = new Scanner(System.in);
    static CarBookingService carBookingService = new CarBookingService();

    //main menu
    public static void userInterface() {
        int choice =0; boolean success = false;
        while (choice != 8 || success==false) {
            CLI.mainMenu();
            System.out.println("Type your choice...");
            if(scanner.hasNextInt()) {
                choice = CLIService.checkChoice(scanner, 1, 8);
                success = CLIService.manageChoice(choice);
            }else {
                scanner.nextLine();
                System.out.println("Typing Error!!");
            }
            System.out.println("Please type a number between 1 and 7 for operations or 8 to exit:");
        }
        scanner.close();
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
            case 1 -> manageBookCar(scanner);
            case 2 -> true;
            case 3 -> true;
            case 4 -> true;
            case 5 -> true;
            case 6 -> true;
            case 7 -> true;
            case 8 -> true;
            default -> false;
        };
    }

    //manage the user's choice from the car menu
    public static String manageCarChoice(int choice, Car[] cars) {
        return switch (choice) {
            case 1 -> cars[0].getId().toString();
            case 2 -> cars[1].getId().toString();
            case 3 -> cars[2].getId().toString();
            case 4 -> cars[3].getId().toString();
            case 5 -> cars[4].getId().toString();
            default -> "";
        };
    }

    //user choice to book a car
    private static boolean manageBookCar(Scanner scanner) {

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate toDay = LocalDate.now();

        String userName = validateUserName(scanner);
        if(userName.equalsIgnoreCase("Cancel")) {
            System.out.println("Operation Cancelled");
            return false;
        }

        String[] serviceDates = validateServiceDates(scanner, toDay);
        if(serviceDates[0] == null || serviceDates[1] == null) {
            System.out.println("ERROR!! operation cancelled cause the start date is after the end date or the start date is in the past");
            return false;
        }

        Car[] availableCars = carBookingService.getAvailableCars(serviceDates);
        if(availableCars.length == 0) {
            System.out.println("No cars available for the selected period");
            return false;
        }

        String carId = validateChosenCar(scanner, availableCars);
        if(!carId.isEmpty()) {
            User user = createUser(userName);
            CarBooking booking =
                    carBookingService.bookCar(user.getId().toString(), carId, serviceDates[0], serviceDates[1], toDay.format(formater)).orElse(null);
            if(booking != null) {
                String[] elementsToPrint = createBookingElements(user, booking, carBookingService, formater);
                CLI.printBooking(elementsToPrint);
            }
            else {
                System.out.println("ERROR!! Operation Failed");
                return false;
            }
        }

        return true;
    }

    //Ask for user name and validate the input
    private static String validateUserName(Scanner scanner) {
        if(scanner.hasNextLine()) scanner.nextLine();
        System.out.println("\n        Rental Car      ");
        System.out.println("Type your name: "); //TODO update for seeded usersà
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank() || userInput.length() < 3 ) {
            System.out.println("Error! Name cannot be empty and should be at least of three characters, please type your name or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        return userInput;
    }

    //Ask for service dates and validate the input
    private static String[] validateServiceDates(Scanner dateScanner, LocalDate toDay) {
        String[] dates = new String[2];
        String startDate, endDate ;

        System.out.println("\n      Rental Car       ");
        System.out.println("The start date of the service... ");
        do {
            startDate = createDate(dateScanner);
        } while (startDate.isEmpty() || startDate.isBlank());

        System.out.println("\n      Rental Car       ");
        System.out.println("The end date of the service... ");
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
        System.out.println("type  the day of the month: ");
        String day = scanner.nextLine() ;
        System.out.println("type  the month of the year: ");
        String month = scanner.nextLine() ;
        System.out.println("type  the year:");
        String year = scanner.nextLine() ;
        if(month.length() == 1) month = "0" + month;
        if(day.length() == 1) day = "0" + day;
        date += day + "-" + month + "-" + year;
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("The date: "+ date +" is not valid, please type a valid date:");
            date = "";
        }
        return date;
    }

    private static String validateChosenCar(Scanner scanner, Car[] availableCars) {
        int max = availableCars.length + 1;
        int choice =0; String  carId = "";
        while (choice != max && carId.isEmpty()) {
            CLI.carMenu(availableCars);
            System.out.println("Type your choice...");
            if(scanner.hasNextInt()) {
                choice = CLIService.checkChoice(scanner, 1, max);
                carId = CLIService.manageCarChoice(choice, availableCars);
            }else {
                scanner.nextLine();
                System.out.println("Typing Error!!");
                choice = 0;
                System.out.println("Please type a number between 1 and " + (max-1) + " for operations or " + max + " to abort the booking operation..");
            }
        }

        return carId;
    }

    //create a user and add it to the datasource
    private static User createUser(String name) {
        return new User(name);
    }

    private static String[] createBookingElements(User user, CarBooking booking, CarBookingService carBookingService, DateTimeFormatter formater) {
        String[] elementsToPrint = new String[5];
        elementsToPrint[0] = user.getId().toString();
        elementsToPrint[1] = carBookingService.getCarById(booking.getCarID().toString()).getName();
        elementsToPrint[2] = booking.getStartDate().format(formater);
        elementsToPrint[3] = booking.getEndDate().format(formater);
        elementsToPrint[4] = booking.getRentalPrice().toString();
        return elementsToPrint;
    }
}
