package com.intuit.ordermanagement.core.dao;

import com.intuit.ordermanagement.core.entities.OrderProductsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderProductsMappingDao extends JpaRepository<OrderProductsMapping,Long> {

    OrderProductsMapping findByOrderId(String orderId);


}
