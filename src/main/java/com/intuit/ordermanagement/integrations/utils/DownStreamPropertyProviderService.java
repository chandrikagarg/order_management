package com.intuit.ordermanagement.integrations.utils;

import lombok.Getter;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
public class DownStreamPropertyProviderService {

//    @Value("${downstream-get-final-price-url:final/price/url}")
    private String getFinalPriceDetailsUrl;



}
