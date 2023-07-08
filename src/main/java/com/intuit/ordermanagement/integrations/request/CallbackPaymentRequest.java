package com.intuit.ordermanagement.integrations.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallbackPaymentRequest {
    private String requestId;
    private String paymentStatus;
}
