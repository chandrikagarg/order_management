package com.intuit.ordermanagement.integrations.service;

import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;

public interface IDownStreamIntegration {
    PriceDetailsResponse getFinalProductDetails(PriceDetailsRequest priceDetailsRequest) throws Exception;

    Object placeOrder(PlaceOrderRequest placeOrderRequest) throws Exception;
}
