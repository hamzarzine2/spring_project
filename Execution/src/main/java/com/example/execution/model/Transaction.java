package com.example.execution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Transaction {
    private String ticker;
    private String seller;
    private String buyer;
    private String buy_order_guid;
    private String sell_order_guid;
    private int quantity;
    private int price;
}
