package com.gianfrancomanca.booking;



import java.io.*;
import java.time.LocalDate;
import java.util.Optional;

public class CarBookingFileDataAccesService  implements CarBookingDao{
    private static int index = 0;
    private CarBooking[] bookings;
    private final String filePath;

    public CarBookingFileDataAccesService(String filePath) {
        this.filePath = filePath;
        readFromFile();
        index = bookings.length;
    }
    @Override
    public CarBooking[] getBookings() {
        readFromFile();
        setStatus();
        return bookings;
    }

    @Override
    public void addBooking(CarBooking booking) {
        readFromFile();
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
        writeToFile(bookings);
    }

    @Override
    public Optional<CarBooking> getBookingById(String id) {
        readFromFile();
        setStatus();
        for(CarBooking booking : bookings) {
            if(booking.getId().toString().equals(id)) return Optional.of(booking);
        }
        return Optional.ofNullable(null);
    }

    @Override
    public boolean cancelBookingById(String id) {
        readFromFile();
        for (CarBooking booking : bookings) {
            if (booking.getId().toString().equals(id) && booking.getStatus() != BookingStatus.ACTIVE && booking.getStatus() != BookingStatus.COMPLETED) {
                booking.setStatus(BookingStatus.CANCELLED);
                writeToFile(bookings);
                return true;
            }
        }
        return false;
    }

    @Override
    public CarBooking[] getBookingsByUserId(String userId) {
        readFromFile();
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

    private void writeToFile(CarBooking[] bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.out.println("exception occurred");
        }
    }

    private void readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            bookings = (CarBooking[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("exception occurred\n" + e );
        }
    }

//    static void main() {
//        CarBooking[] bookings = new CarBooking[0];
//
//        try (FileOutputStream fos = new FileOutputStream("src/main/resources/car-data-source.cs", true); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            System.out.println("Writing to file...");
//            oos.writeObject(bookings);
//            System.out.println("Done.");
//        } catch (IOException e) {
//            System.out.println("exception occurred");
//        }
//
//        try(FileInputStream fin = new FileInputStream("src/main/resources/car-data-source.cs"); ObjectInputStream ois = new ObjectInputStream(fin)) {
//            System.out.println("Reading from file...");
//            bookings = (CarBooking[]) ois.readObject();
//            System.out.println(Arrays.toString(bookings));
//            System.out.println("Done.");
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("exception occurred\n" + e );
//        }
//
//    }
}
