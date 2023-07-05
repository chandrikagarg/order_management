//package com.intuit.ordermanagement.core.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import org.springframework.data.annotation.Id;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@Table(name = "auth_entity")
//@NoArgsConstructor
//@AllArgsConstructor
//@Setter
//@Getter
//@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class AuthEntity{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "token_id")
//    private String tokenId;
//
//    @Column(name = "email")
//    private String email;
//
//
//    @Column(name = "hash_id")
//    private String hashId;
//
//    @Column(name = "client_id")
//    private int clientId;
//
//    @Column(name = "primary_cell")
//    private String primaryCell;
//
//    @CreationTimestamp
//    @Column(name = "created_at")
//    private Date createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Date updatedAt;
//
//}