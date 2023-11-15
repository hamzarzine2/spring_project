package com.example.wallet;

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
@Entity(name = "wallets")
public class Wallet {

  @Id
  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private double unitValue;
}
