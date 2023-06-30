package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.dao.ProductsDao;
import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.integrations.service.IDownStreamIntegration;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    ProductsDao productsDao;

    @Autowired
    IDownStreamIntegration downStreamIntegration;

    @Override
    public List<Product> findByCategory(CategoryEnum category) {
       return  productsDao.findByCategory(category);
    }

    @Override
    public Object findFinalPriceForProducts(PriceDetailsRequest priceDetailsRequest) throws Exception {
        return downStreamIntegration.getFinalProductDetails(priceDetailsRequest);
    }

    @Override
    public Object placeOrderForProducts(PlaceOrderRequest placeOrderRequest) throws Exception {
        return downStreamIntegration.placeOrder(placeOrderRequest);
    }
}
