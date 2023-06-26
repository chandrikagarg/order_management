package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.service.enums.CategoryEnum;

import java.util.List;

public interface IProductService {

    List<Product> findByCategory(CategoryEnum category);
}
