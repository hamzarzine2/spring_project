package com.example.wallet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "positions")
public class Position {

  @Id
  @Column(nullable = false)
  private int id;

  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private double quantity;

  @Column(nullable = false)
  private double unitValue;

  @Column(nullable = false)
  private String username;

  public double getNetWorth() {
    return quantity * unitValue;
  }

  public Position toEntity(PositionDTO positionDTO) {
    this.ticker = positionDTO.getTicker();
    this.quantity = positionDTO.getQuantity();
    this.unitValue = positionDTO.getUnitValue();
    return this;
  }
}
