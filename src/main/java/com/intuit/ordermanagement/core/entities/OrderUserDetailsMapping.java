package com.intuit.ordermanagement.core.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "order_user_details_mapping")
public class OrderUserDetailsMapping {

    private static final long serialVersionUIZD = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "address_id")
    private String addressId;

}
