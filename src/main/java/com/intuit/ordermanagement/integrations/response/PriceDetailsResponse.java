package com.intuit.ordermanagement.integrations.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDetailsResponse extends  DownStreamServiceBaseResponse{
    private Double price;
    private Double taxes;
    private Double totalPrice;
}
