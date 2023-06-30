package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.service.enums.CategoryEnum;

import java.util.List;

public interface IProductService {

    List<Product> findByCategory(CategoryEnum category);

    Object findFinalPriceForProducts(PriceDetailsRequest priceDetailsRequest) throws Exception;

    Object placeOrderForProducts(PlaceOrderRequest placeOrderRequest) throws Exception;
}
