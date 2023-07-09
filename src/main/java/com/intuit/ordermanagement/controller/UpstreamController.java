package com.intuit.ordermanagement.controller;

import com.intuit.ordermanagement.integrations.request.CallbackPaymentRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.service.IUpstreamService;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/osm")
public class UpstreamController {

    @Autowired
    IUpstreamService upstreamService;

//    @PostMapping(value = "/category")
//    public Object findAllProducts(@RequestParam String category){
//        return upstreamService.findByCategory(CategoryEnum.valueOf(category));
//    }
    @GetMapping("/products/{productId}/price")
    public Object findFinalPrice(@RequestHeader String userId, @RequestParam String addressId, @PathVariable String productId) throws Exception {
        return upstreamService.findFinalPriceForProducts(productId,userId,addressId);
    }
    @PostMapping(value = "/order")
    public Object placeOrder(@RequestHeader String userId, @RequestBody PlaceOrderRequest placeOrderRequest) throws Exception {
        return upstreamService.placeOrderForProducts(userId, placeOrderRequest);
    }
    @GetMapping("/order/{orderId}")
    public Object findFinalPrice(@RequestHeader String userId, @PathVariable String orderId) throws Exception {
        return upstreamService.getOrderStatus(userId, orderId);
    }
    @PostMapping("/callback")
    public Object getCallbackForPayment(@RequestHeader String userId, @RequestBody CallbackPaymentRequest callbackPaymentRequest) throws Exception {
        //TODO It will be consumed through queue kafka
        return upstreamService.updatePaymentStatus(userId, callbackPaymentRequest);
    }


}
