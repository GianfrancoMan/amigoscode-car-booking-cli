package com.gianfrancomanca.car;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Car implements Serializable {
    private UUID id;
    private String name;
    private String registrationNumber;
    private BigDecimal rentalPrice;
    private boolean electric;
    public Brand brand;

    public Car(UUID id, Brand brand, String name, String registrationNumber, BigDecimal rentalPrice, boolean electric) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.rentalPrice = rentalPrice;
        this.electric = electric;
    }

    public String getName() {
        return name;
    }

    public Brand getBrand() {
        return brand;
    }

    public UUID getId() {
        return id;
    }

    public String getRegistrationNumber() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
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
        return Objects.equals(getId(), car.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRegistrationNumber());
    }
}
