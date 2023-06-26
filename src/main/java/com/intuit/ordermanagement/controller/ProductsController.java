package com.intuit.ordermanagement.controller;

import com.intuit.ordermanagement.service.IProductService;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/products")
public class ProductsController {

    @Autowired
    IProductService productService;

    @PostMapping(value = "/category")
    public Object findAllProducts(@RequestParam String category){
        return productService.findByCategory(CategoryEnum.valueOf(category));
    }
}
