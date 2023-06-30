package com.intuit.ordermanagement.integrations.request;

import com.intuit.ordermanagement.core.dto.AdressDetails;
import com.intuit.ordermanagement.core.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    private String pgId;
    private AdressDetails adressDetails;
    private List<Product> productList;
}
