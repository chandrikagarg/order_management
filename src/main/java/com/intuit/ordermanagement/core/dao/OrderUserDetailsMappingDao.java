package com.intuit.ordermanagement.core.dao;

import com.intuit.ordermanagement.core.entities.OrderUserDetailsMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderUserDetailsMappingDao extends JpaRepository<OrderUserDetailsMapping,Long> {


}
