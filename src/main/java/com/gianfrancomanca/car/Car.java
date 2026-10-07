package com.gianfrancomanca.car;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Car {
    private UUID id;
    private java.lang.String name;
    private java.lang.String registrationNumber;
    private BigDecimal rentalPrice;
    private boolean electric;
    public Brand brand;

    public Car(Brand brand, java.lang.String name, java.lang.String registrationNumber, BigDecimal rentalPrice, boolean electric) {
        this.brand = brand;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.rentalPrice = rentalPrice;
        this.electric = electric;
        id = UUID.randomUUID();
    }

    public java.lang.String getName() {
        return name;
    }

    public Brand getBrand() {
        return brand;
    }

    public UUID getId() {
        return id;
    }

    public java.lang.String getRegistrationNumber() {
        return registrationNumber;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public boolean isElectric() {
        return electric;
    }

    public void setElectric(boolean electric) {
        this.electric = electric;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setRegistrationNumber(java.lang.String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public void setEletric(boolean eletric) {
        this.electric = eletric;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", electric=" + electric +
                ", brand=" + brand +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Car car)) return false;
        return Objects.equals(getId(), car.getId()) && Objects.equals(getRegistrationNumber(), car.getRegistrationNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRegistrationNumber());
    }
}
