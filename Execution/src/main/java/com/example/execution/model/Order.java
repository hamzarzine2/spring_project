package com.example.execution.model;

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
public class Order {
    private String id;
    private String owner;
    private long timestamp;
    private String ticker;
    private int quantity;
    private Side side;
    private Type type;
    private long limit;
    private int filled;


}
