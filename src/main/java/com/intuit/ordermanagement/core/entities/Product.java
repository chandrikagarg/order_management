package com.intuit.ordermanagement.core.entities;


import com.intuit.ordermanagement.service.enums.CategoryEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.lang.reflect.Type;

@Entity
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "products")
public class Product implements Serializable{

    private static final long serialVersionUIZD = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CategoryEnum category;

    @Column(name = "price")
    private double price;


}
