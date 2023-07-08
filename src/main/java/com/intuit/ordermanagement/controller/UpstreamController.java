package com.intuit.ordermanagement.controller;

import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.service.IProductService;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/osm")
public class UpstreamController {

    @Autowired
    IProductService productService;

    @PostMapping(value = "/category")
    public Object findAllProducts(@RequestParam String category){
        return productService.findByCategory(CategoryEnum.valueOf(category));
    }
    @GetMapping("/products/{productId}/price")
    public Object findFinalPrice(@RequestHeader String userId, @RequestParam String addressId, @PathVariable String productId) throws Exception {
        return productService.findFinalPriceForProducts(productId,userId,addressId);
    }
    @PostMapping(value = "/placeOrder")
    public Object placeOrder(@RequestHeader String userId, @RequestBody PlaceOrderRequest placeOrderRequest) throws Exception {
        return productService.placeOrderForProducts(userId, placeOrderRequest);
    }


}
