package com.example.wallet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;

  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private double quantity;

  @Column(name = "unit_value")
  private double unitValue;

  @Column(nullable = false)
  private String username;

  public double getNetWorth() {
    return quantity * unitValue;
  }

}
