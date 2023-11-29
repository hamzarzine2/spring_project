package com.example.wallet.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PositionDTO {
  private String ticker;
  private double quantity;
  private double unitValue;

  public PositionDTO toDto(Position position) {
    this.ticker = position.getTicker();
    this.quantity = position.getQuantity();
    this.unitValue = position.getUnitValue();
    return this;
  }
}
