package com.intuit.ordermanagement.controller;

import com.intuit.ordermanagement.core.dto.AdressDetails;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.service.IProductService;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/products")
public class ProductsController {

    @Autowired
    IProductService productService;

    @PostMapping(value = "/category")
    public Object findAllProducts(@RequestParam String category){
        return productService.findByCategory(CategoryEnum.valueOf(category));
    }
    @PostMapping(value = "/getFinalPrice")
    public Object findFinalPrice(@RequestBody PriceDetailsRequest priceDetailsRequest){
        return productService.findFinalPriceForProducts(priceDetailsRequest);
    }

}
