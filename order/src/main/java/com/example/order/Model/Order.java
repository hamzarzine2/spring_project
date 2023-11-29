package com.example.order.Model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    private String owner;
    private long timestamp;
    private String ticker;
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_side", length = 10)
    private Side side;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 10)
    private Type type;

    @Column(name = "order_limit")
    private long limit;
    private int filled;


}
