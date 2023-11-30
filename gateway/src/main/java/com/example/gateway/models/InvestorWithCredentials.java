package com.example.gateway.models;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InvestorWithCredentials {
  private String username;
  private String email;
  private String firstname;
  private String lastname;
  private LocalDate birthdate;
  private String password;

  public Investor toUser() {
    return new Investor(username, firstname, lastname, email, birthdate);
  }
  public Credentials toCredentials() {
    return new Credentials(username, password);
  }

}
