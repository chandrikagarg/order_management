package com.intuit.ordermanagement.core.dao;

import com.intuit.ordermanagement.core.entities.Orders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersDao extends PagingAndSortingRepository<Orders,Long> {

}
