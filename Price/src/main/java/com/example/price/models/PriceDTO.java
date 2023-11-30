package com.example.price.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceDTO {
  private String ticker;
  private double price;

  public void toDto(Price price) {
    this.ticker = price.getTicker();
    this.price = price.getPrice();
  }
}
