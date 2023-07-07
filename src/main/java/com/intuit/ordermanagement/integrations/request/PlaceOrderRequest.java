package com.intuit.ordermanagement.integrations.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    private String orderId;
    private String addressId;
    private Double amount;
    private String productId;
    private String userId;
}
