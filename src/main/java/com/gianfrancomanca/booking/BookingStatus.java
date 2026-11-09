package com.gianfrancomanca.booking;

public enum BookingStatus {
    BOOKED("Booked", 1),
    ACTIVE("Active", 0),
    CANCELED( "Canceled", 3),
    COMPLETED( "Completed", 2);

    private String statusName;
    private int statusOrdinal;
    BookingStatus(String statusName, int statusOrdinal) {
        this.statusName = statusName;
        this.statusOrdinal = statusOrdinal;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getStatusOrdinal() {
        return statusOrdinal;
    }
}
