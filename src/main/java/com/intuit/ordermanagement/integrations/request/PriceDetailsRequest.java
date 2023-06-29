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
public class PriceDetailsRequest {
    AdressDetails adressDetails;
    List<Product> productList;
}
