package com.intuit.ordermanagement.service.enums;

public enum OrderStatusEnum {
    INITIATED,
    PAYMENT_REQ_ACK,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    DISPATCHED,
    SHIPPED,
    DELIVERED,
    OUT_FOR_DELIVERY,
    REFUNDED,
    PENDING;
}
