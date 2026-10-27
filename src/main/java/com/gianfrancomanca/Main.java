package com.gianfrancomanca;

import com.gianfrancomanca.booking.*;
import com.gianfrancomanca.car.*;
import com.gianfrancomanca.user.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    private Scanner scanner;
    private CarBookingService carBookingService;
    private UserService userService;
    private CarService carService;
    private DateTimeFormatter formater;
    LocalDate toDay;

    public Main(Scanner scanner, CarBookingService carBookingService, UserService userService, CarService carService) {
        this.scanner = scanner;
        this.carBookingService = carBookingService;
        this.userService = userService;
        this.carService = carService;
        this.formater = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.toDay = LocalDate.now();
    }

    /***MAIN METHOD***/
    static void main() {
        Main mainReference = new Main(
                new Scanner(System.in),
                new CarBookingService(new CarBookingFileDataAccessService("src/main/resources/carbooking-file-data-source.cs")),
                new UserService(new UserFileDataSourceService("src/main/resources/user-file-data-source.cs")),
                new CarService(new CarFileDataSourceService("src/main/resources/car-file-data-source.cs")));
        mainReference.userInterface();
    }

    //main menu
    public void userInterface() {
        int choice =0;
        while (choice != 8) {
            printSentence("\nPlease type a number between 1 and 7 for operations or 8 to exit:");
            mainMenu();
            printSentence("Type your choice then press enter: ");
            if(scanner.hasNextInt()) {
                choice = checkChoice(scanner, 1, 8);
                manageChoice(choice);
            }else {
                scanner.nextLine();
            }
        }
        scanner.close();
        printSentence("Thank you for using our service, see you soon!\n");
    }


    //user choice to book a car
    private boolean manageBookCarView(Scanner scanner) {

        String userId = validateUserId(scanner);
        String username = "";
        User user= userService.getUserById(userId);
        username = user.getName();
        if(userId.equalsIgnoreCase("Cancel") || username.equalsIgnoreCase("not found")) {
            printError(userId.equalsIgnoreCase("Cancel") ? "Operation Cancelled" : "Unique identifier you provided is not valid, please try again");
            return false;
        }

        Optional<LocalDate[]> serviceDates = validateServiceDates(scanner, username);
        if(serviceDates.isEmpty()) {
            printError("ERROR!! operation cancelled cause the start date is after the end date or the start date is in the past");
            return false;
        }

        List<Car> availableCars = carBookingService.getAvailableCars(serviceDates.get(), carService.getCars());
        if(availableCars.isEmpty()) {
            printSentence("No cars available for the selected period");
            return false;
        }

        String carId = validateChosenCar(scanner, availableCars);
        if(!carId.isEmpty()) {
            user = userService.getUserById(userId);
            Car car = carService.getCarById(carId).orElse(null);
            CarBooking booking =
                    carBookingService.bookingCar(user, car,serviceDates.get()[0], serviceDates.get()[1]).orElse(null);
            if(booking != null) {
                String[] elementsToPrint = createBookingElements(user, booking, carBookingService, formater);
                printBooking(elementsToPrint);
            }
            else {
                printError("ERROR Operation Failed!!  please check the data you provided and try again.");
                return false;
            }
        }

        return true;
    }

    //user choice to see all the bookings
    private boolean manageAllBookingView() {
        List<CarBooking> bookings = carBookingService.getAllBookings();
        if(!bookings.isEmpty()) {
            List<String> data = new ArrayList<>();
            int i= 1;
            for(CarBooking booking : bookings){
                Car car = carService.getCarById(booking.getCarID()).orElse(null);
                if(null == car) {
                    data.add("Not Found");
                }
                else {
                    data.add("\n(" + i + ")\nBOOKING date: " + parseDateForView(booking.getBookingDate()) + "\t\t id: " + booking.getId().toString()
                            + "\nCUSTOMER\t\tname: " + userService.getUserById(booking.getUserId()).getName() + "\t\tid: " + booking.getUserId()
                            +"\nCAR\t\t model: "  + car.getBrand().getName() + " " + car.getName() + "\t\tregistration number: " + car.getRegistrationNumber()
                            +"\nBOOKING PERIOD\t\tstart: " + parseDateForView(booking.getStartDate()) + "\t\tend:  " + parseDateForView(booking.getEndDate())
                            +"\nSTATUS: " + booking.getStatus().getStatusName());
                }
                i++;
            }
            printAllBookings(data);
            return true;
        }
        printError("Something was wrong, please try again later");
        return false;
    }

    //user choice to delete a booking
    private boolean manageDeleteBookingView(Scanner scanner) {
        printSentence("Type the booking ID to delete it or \"Cancel\" to abort the operation:");
        if(scanner.hasNextLine()) scanner.nextLine();
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank()) {
            printError("Error! The Booking ID cannot be empty, please type the  booking ID or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        if(!userInput.equalsIgnoreCase("cancel"))  {
            boolean deleted = carBookingService.cancelBooking(userInput);
            if(deleted) {
                printSentence("Booking deleted successfully");
                return true;
            }
            printError("Operation failed!! possible causes: booking not exists or the booking is currently active, completed or already canceled");
        }
        return true;
    }

    //user choice to see all booking of a cutomer
    private boolean manageUsersBooking(Scanner scanner) {
        printSentence("Type the user ID to see its bookings or \"Cancel\" to abort the operation:");
        if(scanner.hasNextLine()) scanner.nextLine();
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank()) {
            printError("Error! The user ID cannot be empty, please type the  user ID or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        if(!userInput.equalsIgnoreCase("cancel"))  {
            List<CarBooking> userBookings = carBookingService.getBookingsByUser(userInput);
            if(!userBookings.isEmpty()) {
                String customerName =
                        userService.getUserById(userInput).getName().equalsIgnoreCase("not found") ? "customer" : userService.getUserById(userInput).getName();
                printSentence("\nBookings made by "+ customerName + "...");
                var data = new ArrayList<String>();
                int i = 1;
                for(CarBooking userBooking : userBookings) {
                    Car car = carService.getCarById(userBooking.getCarID()).orElse(null);
                    if(null == car) {
                        data.add("Not Found");
                    }
                    else {
                        data.add("\n(" + (i) + ")\nBOOKING date: " + parseDateForView(userBooking.getBookingDate()) + "\t\t id: " + userBooking.getId().toString()
                                + "\nCAR\t\t model: " + car.getBrand().getName() + " " + car.getName() + "\t\tregistration number: " + car.getRegistrationNumber()
                                +"\nBOOKING PERIOD\t\tstart: " + parseDateForView(userBooking.getStartDate()) + "\t\tend:  " + parseDateForView(userBooking.getEndDate())
                                +"\nSTATUS: " + userBooking.getStatus().getStatusName());
                    }
                    i++;
                }
                printAllBookings(data);
                return true;
            } else
                printSentence("Operation failed!! No bookings found for the user or the user does not exist\n");
        } else
            printSentence("Operation cancelled successfully\n");

        return true;
    }

    //user choice to see all the cars of the fleet
    private boolean manageAllCarView() {
        printHeader(
                "\n--------------------------------------------------------------"
                        + "\nAVAILABLE AND PARTIALLY AVAILABLE CARS ..."
                        + "\n---------------------------------------------------------------\n");
        printArray(getCarsAvailabilities());
        return  true;
    }

    private boolean manageAllElectricCarView() {
        printHeader(
                "\n-----------------------------------------------------------------------"
                        + "\nAVAILABLE AND PARTIALLY AVAILABLE ELECTRIC CARS ..."
                        + "\n------------------------------------------------------------------------\n");
        printArray(getElectricCarsAvailabilities());
        return true;
    }

    private boolean manageAllUserView() {
        List<User> users = userService.getAllUsers();
        List<String> userData = new ArrayList<>();
        if(!users.isEmpty()) {
            printHeader(
                    "\n-----------------------------------------------"
                            + "\nRENTAL CAR  -> CUSTOMER LIST\n"
                            + "----------------------------------------------\n"
            );
            printArray(createUserData(users, userData));
        }
        return true;
    }

    //check if the user typed a valid number
    private int checkChoice(Scanner scanner, int min, int max) {
        int choice = scanner.nextInt();
        if (choice < min || choice > max) {
            return 0;
        }
        return choice;
    }

    //manage the user's choice from the main menu'
    private void manageChoice(int choice) {
        switch (choice) {
            case 1 -> manageBookCarView(scanner);
            case 2 -> manageDeleteBookingView(scanner);
            case 3 -> manageUsersBooking(scanner);
            case 4 ->  manageAllBookingView();
            case 5 -> manageAllCarView();
            case 6 -> manageAllElectricCarView();
            case 7 -> manageAllUserView();
        }
    }

    //manage the user's choice from the car menu
    private String manageCarChoice(int choice, List<Car> cars) {
        for (int i=0; i<cars.size(); i++) {
            if(choice == (i+1)) return cars.get(i).getId().toString();
        }
        return "";
    }

    //create the user data array
    private List<String> createUserData(List<User> users, List<String> userData) {
        for(int i=0; i<users.size(); i++) {
            userData.add(String.format(" (%d)  Name: %-50s ID: %-50s", (i+1), users.get(i).getName(), users.get(i).getId().toString()));
        }
        return userData;
    }

    //Ask for user name and validate the input
    private String validateUserId(Scanner scanner) {
        if(scanner.hasNextLine()) scanner.nextLine();
        printSentence("\n        Rental Car      \nType customer ID or \"Cancel\" to cancel the operation: ");
        String userInput = scanner.nextLine();
        while(userInput.isEmpty() || userInput.isBlank() || userInput.length() < 3 ) {
            printError("Error! Unique user ID cannot be empty, please type the unique ID or \"Cancel\" to abort booking:");
            userInput = scanner.nextLine();
        }
        return userInput;
    }

    //Ask for service dates and validate the input
    private Optional<LocalDate[]> validateServiceDates(Scanner dateScanner, String username) {
        Optional<LocalDate[]> dates = Optional.of(new LocalDate[2]);
        String startDate, endDate ;

        printSentence("\n      Rental Car ->        " + username + "\nThe start date of the service... ");
        do {
            startDate = createDate(dateScanner);
        } while (startDate.isEmpty() || startDate.isBlank());

        printSentence("\n      Rental Car       " + "\nThe end date of the service... ");
        do {
            endDate = createDate(dateScanner);
        } while (endDate.isEmpty() || endDate.isBlank());

        return carBookingService.validateInputDates(formater, startDate, endDate);
    }

    //create a date from the user input
    private String createDate(Scanner scanner) {
        String date = "";
        printSentence("type  the day of the month: ");
        String day = scanner.next() ;
        printSentence("\t\tthe month of the year: ");
        String month = scanner.next() ;
        printSentence("\t\tthe year:");
        String year = scanner.next() ;
        if(month.length() == 1) month = "0" + month;
        if(day.length() == 1) day = "0" + day;
        date += day + "-" + month + "-" + year;
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            printError("The date: "+ date +" is not valid, please type a valid date:");
            date = "";
        }
        return date;
    }

    private String validateChosenCar(Scanner scanner, List<Car> availableCars) {
        int max = availableCars.size() + 1;
        int choice =0; String  carId = "";
        while (choice != max && carId.isEmpty()) {
            printHeader("\n\n           CARS AVAILABLE FOR THE SELECTED PERIOD   \n------------------------------------------------------------------");
            carMenu(availableCars);
            printSentence("Type your choice then press enter: ");
            if(scanner.hasNextInt()) {
                choice = checkChoice(scanner, 1, max);
                carId = manageCarChoice(choice, availableCars);
            }else {
                scanner.nextLine();
                choice = 0;
                printError("Typing Error!!\nPlease type a number between 1 and " + (max-1) + " for operations or " + max + " to abort the booking operation..");
            }
        }

        return carId;
    }

    private String[] createBookingElements(User user, CarBooking booking, CarBookingService carBookingService, DateTimeFormatter formater) {
        String[] elementsToPrint = new String[6];
        elementsToPrint[0] = user.getId().toString();
        elementsToPrint[1] = user.getName();
        Car car = carService.getCarById(booking.getCarID().toString()).orElse(null);
        elementsToPrint[2]  = (null == car)
                ? "Not Found"
                :  car.getBrand().getName() + " " + car.getName()+ "\t\tregistration number: " +car.getRegistrationNumber();
        elementsToPrint[3] = booking.getStartDate().format(formater);
        elementsToPrint[4] = booking.getEndDate().format(formater);
        elementsToPrint[5] = booking.getRentalPrice().toString();
        return elementsToPrint;
    }

    private String parseDateForView(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    //print the main menu
    public void mainMenu() {
        System.out.println("\n| ---------------------------------------|");
        System.out.println("          Welcome to Car Rental!          ");
        System.out.println("-----------------------------------------");
        System.out.println("|   1 to book a car                            |");
        System.out.println("|   2 to delete a booking                 |");
        System.out.println("|   3 See all rentals of a customer |");
        System.out.println("|   4 to view all bookings                 |");
        System.out.println("|   5 to view available cars              |");
        System.out.println("|   6 to view available electric cars |");
        System.out.println("|   7 to view all users                       |");
        System.out.println("|   8 to exit                                      |");
        System.out.println("| ------------------ ------------------- |");
    }

    //print the Car Menu
    public void carMenu(List<Car> cars) {
        int i = 1;
        for (Car car : cars) {
            System.out.println(
                    "    Type " + i + " to choose " + car.getBrand().getName()
                            + " " + car.getName() + " "
                            + (car.isElectric() ? " - Electric: Yes " : " Electric: No ")
                            + " - Price: " + car.getRentalPrice() + "€ (per day)"
            );
            ++i;
        }            System.out.println("    Type " + (cars.size() +1)+ " to Cancel");
        System.out.println("---------------------------------------------------------------");
    }

    public void printBooking(String[] data) {

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

    public void printArray(List<String> data) {
        if(data.isEmpty()) {
            System.out.println("Nothing to display.\n");
        }
        else {
            for (String element : data) System.out.println(element);
        }
        System.out.println("\n");
    }

    public void printAllBookings(List<String> bookingsData) {
        System.out.println("                Rental Car Bookings List:");
        System.out.println("-----------------------------------------------------------");
        if(bookingsData.isEmpty()) {
            System.out.println("No bookings found");
            System.out.println("-----------------------------------------------------------");
            return;
        }
        for (String booking : bookingsData) System.out.println(booking);
        System.out.println("-----------------------------------------------------------");
    }

    public void printError(String error) {
        System.out.println("                Rental Car Booking Error:");
        System.out.println("-----------------------------------------------------------");
        System.out.println(error);
    }

    public void printHeader(String header) {
        System.out.println(header);
    }

    public void printSentence(String sentence) {
        System.out.print(sentence);
    }

    //get all cars availabilities
    private List<String> getCarsAvailabilities() {
        List<String> carIds  = carService.retrieveCarIds();
        List<String> availabilities = new ArrayList<>();
        for(int i=0; i<carIds.size(); i++) {
            availabilities.add("("+ (i + 1) + ")" +createAvailabilities(carIds.get(i)));
        }

        return availabilities;
    }

    //get all electric cars availabilities
    private List<String> getElectricCarsAvailabilities() {
        List<String> eIds = carService.retrieveECarsIds();
        List<String> availabilities = new ArrayList<>();
        for(int i=0; i<eIds.size(); i++) {
            availabilities.add("("+ (i + 1) + ")" +createAvailabilities(eIds.get(i)));
        }

        return availabilities;
    }

    //Create a string with the car availability
    private String createAvailabilities(String carId) {
        String checkedIds = "";
        String availability = "";
        String partialAvailability = "";
        List<CarBooking> bookings = carBookingService.getAllBookings();

        Car car = carService.getCarById(carId).orElse(null);

        availability += "\n" + car.getBrand().getName() + " " + car.getName() + " ";
        for (CarBooking booking : bookings) {
            if(booking.getCarID().equals(carId)) {
                if(!checkedIds.contains(booking.getCarID())) {
                    checkedIds  = booking.getCarID();
                }
                if(booking.getStatus() == BookingStatus.ACTIVE || booking.getStatus() == BookingStatus.BOOKED) {
                    partialAvailability +=  "\tfrom " + booking.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            + " to " + booking.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            + "\n";
                }
            }
        }
        if(partialAvailability.isEmpty()) {
            availability += "Currently available.\n";
        } else {
            availability += "Available except :\n" + partialAvailability;
        }

        return availability;
    }
}
