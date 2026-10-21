package com.gianfrancomanca.booking;

public enum BookingStatus {
    BOOKED("Booked"),
    ACTIVE("Active"),
    CANCELED( "Canceled"),
    COMPLETED( "Completed");

    private String statusName;
    BookingStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
