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
@Table(name = "orders")
public class Orders{
    private static final long serialVersionUIZD = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_status")
    private OrderStatusEnum orderStatus;


}
