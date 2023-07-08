package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.request.CallbackPaymentRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import com.intuit.ordermanagement.service.response.OrderMgmtAPIResponse;

import java.util.List;

public interface IUpstreamService {

    List<Product> findByCategory(CategoryEnum category);

    OrderMgmtAPIResponse findFinalPriceForProducts(String productId, String userId, String addressId) throws Exception;

    OrderMgmtAPIResponse placeOrderForProducts(String userId, PlaceOrderRequest placeOrderRequest) throws Exception;

    OrderMgmtAPIResponse getOrderStatus(String userId, String orderId);

    OrderMgmtAPIResponse updatePaymentStatus(String userId, CallbackPaymentRequest callbackPaymentRequest) throws Exception;
}
