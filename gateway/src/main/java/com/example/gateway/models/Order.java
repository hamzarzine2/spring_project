package com.example.gateway.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id
  private String guid;
  private String owner;
  private int timestamp;
  private String ticker;
  private int quantity;
  private String side;
  private String type;
  private double limit;
  private int filled;


}
