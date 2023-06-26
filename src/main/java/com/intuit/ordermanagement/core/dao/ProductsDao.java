package com.intuit.ordermanagement.core.dao;

import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsDao extends PagingAndSortingRepository<Product,Long> {

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    List<Product> findByCategory(CategoryEnum category);

}
