package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.dao.ProductsDao;
import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    ProductsDao productsDao;

    @Override
    public List<Product> findByCategory(CategoryEnum category) {
       return  productsDao.findByCategory(category);
    }
}
