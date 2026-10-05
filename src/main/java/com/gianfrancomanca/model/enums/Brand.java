package com.gianfrancomanca.model.enums;

public enum Brand {
    FORD("Ford"),
    AUDI("Audi"),
    MERCEDES("Mercedes"),
    CUPRA("Cupra"),
    FERRARI("Ferrrari"),
    RENAULT("Renault");

    private String name;

    public String getName() {
        return name;
    }

    Brand(String name) {
        this.name = name;
    }
}
