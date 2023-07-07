package com.intuit.ordermanagement.controller;

import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ping")
public class PingController {

    @PostMapping(value = "/test")
    public Object findAllProducts(){
        return null;
    }

}
