package com.example.investor.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "investors")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Investor {

 @Id
 @Column(name = "user_name")
 private String username;
 private String email;

 @Column(name = "birth_date")
 private String birthdate;
 @Column(name = "first_name")

 private String firstname;
 @Column(name = "last_name")

 private  String lastname;

}
