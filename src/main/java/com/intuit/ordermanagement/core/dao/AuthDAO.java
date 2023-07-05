//package com.intuit.ordermanagement.core.dao;
//
//import com.intuit.ordermanagement.core.entities.AuthEntity;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface AuthDAO extends PagingAndSortingRepository<AuthEntity, Long> {
//
//    @Query("select p from AuthEntity p where p.tokenId= :token")
//    AuthEntity get(String token);
//}
