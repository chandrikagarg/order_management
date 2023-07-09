package com.intuit.ordermanagement.service.enums;

public enum ErrorCodeEnum {
    P_01("Product id or address id cannot be null"),
    P_02("Error in getting base price of the product"),
    P_03("Error in getting tax details price of the product"),
    O_01("Unable to create order please try again"),
    O_02("Order id does not exist"),
    O_03("Request id does not exist"),
    O_04("Unable to update the status of the order"),
    O_05("Empty update payment status request received");






    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}