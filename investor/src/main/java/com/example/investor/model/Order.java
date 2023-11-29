package com.example.investor.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
  private String side;


  private String type;


  private long limit;
  private int filled;


}
