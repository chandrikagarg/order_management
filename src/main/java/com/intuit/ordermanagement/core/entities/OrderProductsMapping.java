package com.intuit.ordermanagement.core.entities;

import com.intuit.ordermanagement.service.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "order_products_mapping")
public class OrderProductsMapping {

    private static final long serialVersionUIZD = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_status")
    private OrderStatusEnum productsStatus;
}
