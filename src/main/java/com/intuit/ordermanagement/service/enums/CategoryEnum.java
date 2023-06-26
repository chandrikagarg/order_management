package com.intuit.ordermanagement.service.enums;

public enum CategoryEnum {
    household("HouseHold Items"),
    furniture("furniture"),
    electronics("Electronics Item");

    private String name;

    CategoryEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
