package com.example.gateway.models;

import java.time.LocalDate;
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
public class Investor {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String birthdate;
}
