package com.example.investor;

import com.example.investor.model.Investor;
import com.example.investor.model.InvestorWithPassword;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvestorControlleur {

    @Autowired
   private InvestorService service;
  @GetMapping("/investor/{username}")
  public ResponseEntity<Investor> getOne(@PathVariable String username) {
    Investor investor = service.getOne(username);
    if(investor == null)
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(investor,HttpStatus.OK);
  }

  @PostMapping("/investor/{username}")
  public ResponseEntity<Investor> createOne(@PathVariable String username,@RequestBody InvestorWithPassword investorWithPswrd) {
    if(!isValid(investorWithPswrd.getInvestor_data(),investorWithPswrd.getPassword()))
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    boolean created = service.createOne(investorWithPswrd);
    if(!created)
      return new ResponseEntity<>(HttpStatus.CONFLICT);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/investor/{username}")
  public ResponseEntity<Investor> updateOne(@PathVariable String username, @RequestBody Investor investor) {
    if(!isValid(investor,"investorwithpassword"))
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    boolean updated = service.updateOne(investor);
    if(!updated)
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/investor/{username}")
  public ResponseEntity<Investor> deleteOne(@PathVariable String username) {
    HttpStatus deleted = service.delete(username);

    if(deleted.equals(HttpStatus.NOT_FOUND))
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    else
      if(deleted.equals(HttpStatus.BAD_REQUEST))
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public boolean isValid(Investor investor, String password){
    if(password.equals("investorwithpassword") && password.isBlank())
      return false;

    return !investor.getBirthdate()
        .isBlank()
        && !investor.getEmail().isBlank() && !investor
        .getUsername().isBlank()
        && !investor.getLastname().isBlank() && !investor
        .getFirstName().isBlank();
  }
}
