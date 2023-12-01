package com.example.investor.model;

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

}
