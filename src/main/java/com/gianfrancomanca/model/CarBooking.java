package com.gianfrancomanca.model;

import com.gianfrancomanca.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class CarBooking {
    private String userId;
    private String carID;
    private LocalDate bookingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentalPrice;
    private BookingStatus status;

    public CarBooking(
            String userId, String carID, LocalDate bookingDate, LocalDate startDate, LocalDate endDate, BigDecimal rentalPrice, BookingStatus status) {
        this.userId = userId;
        this.carID = carID;
        this.bookingDate = bookingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    @Override
    public String toString() {
        return "CarBooking{" +
                "userId='" + userId + '\'' +
                ", carID='" + carID + '\'' +
                ", bookingDate=" + bookingDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rentalPrice=" + rentalPrice +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CarBooking booking)) return false;
        return
                Objects.equals(getUserId(), booking.getUserId())
                && Objects.equals(getCarID(), booking.getCarID())
                && Objects.equals(getBookingDate(), booking.getBookingDate())
                && Objects.equals(getStartDate(), booking.getStartDate())
                && Objects.equals(getEndDate(), booking.getEndDate())
                && Objects.equals(getRentalPrice(), booking.getRentalPrice())
                && getStatus() == booking.getStatus();
    }

    @Override
    public int hashCode() {
        return
                Objects.hash(getUserId(), getCarID(), getBookingDate(), getStartDate(), getEndDate(), getRentalPrice(), getStatus());
    }
}
