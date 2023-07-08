package com.intuit.ordermanagement.integrations.service;

import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.response.TaxDetailsResponse;

public interface IDownStreamIntegration {
    PriceDetailsResponse getBasePriceForProduct(String productId, String userId, String addressId) throws Exception;

    TaxDetailsResponse getTaxDetailsForProduct(Double price, String productId, String userId, String addressId) throws Exception;

    DownStreamServiceBaseResponse placeOrder(PlaceOrderRequest placeOrderRequest, String userId) throws Exception;

    DownStreamServiceBaseResponse sendEmail(PlaceOrderRequest placeOrderRequest, String userId) throws Exception;
    DownStreamServiceBaseResponse informOrderInitiation(String userId, OrderInitiationRequest placeOrderRequest) throws Exception;

}
