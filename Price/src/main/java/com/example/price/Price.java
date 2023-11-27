package com.example.price;

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
@Entity(name = "prices")
public class Price {

  @Id
  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private int price;

  public int getPrice(){
    return price;
  }
}
