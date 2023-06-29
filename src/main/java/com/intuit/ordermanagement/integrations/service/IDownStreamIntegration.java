package com.intuit.ordermanagement.integrations.service;

import com.intuit.ordermanagement.core.dto.AdressDetails;
import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;

import java.util.List;

public interface IDownStreamIntegration {
    PriceDetailsResponse getFinalProductDetails(PriceDetailsRequest priceDetailsRequest) throws Exception;
}
