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

  private Investor investor_data;
  private String password;

  public Investor toUser() {
    return investor_data;
  }
  public Credentials toCredentials() {
    return new Credentials(investor_data.getUsername(), password);
  }

}
