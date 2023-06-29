package com.intuit.ordermanagement.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdressDetails {
    private String houseNo;
    private String address;
    private String pincode;
    private String city;
    private String country;
    private String state;
}
