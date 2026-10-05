package com.gianfrancomanca.model.enums;

public enum BookingStatus {
    BOOKED("Booked"),
    ACTIVE("Active"),
    CANCELLED( "Cancelled"),
    COMPLETED( "Completed");

    private String statusName;
    BookingStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
