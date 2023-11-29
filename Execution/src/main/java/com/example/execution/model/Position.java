package com.example.execution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Position {
    private String ticker;
    private double quantity;
    private double unitValue;
}
