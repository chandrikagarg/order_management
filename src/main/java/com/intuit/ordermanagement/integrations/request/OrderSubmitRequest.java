package com.intuit.ordermanagement.integrations.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitRequest {
    private String addressId;
    private Double amount;
    private String productId;
    private String orderId;
}
