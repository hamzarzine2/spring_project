package com.example.investor;

import com.example.investor.repository.InvestorRepository;
import com.example.investor.model.Investor;
import com.example.investor.model.InvestorWithPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
public class InvestorService {
  @Autowired
  private InvestorRepository repository ;


  public Investor getOne(String name){
   return repository.findById(name).orElse(null);
  }

  public boolean createOne(InvestorWithPassword investorWithPswrd){
    Investor investor= repository.findById(investorWithPswrd.getInvestor_data().getUsername()).orElse(null);
    if(investor == null) {
      repository.save(investorWithPswrd.getInvestor_data());
      return true;
    }
    return false;
  }

  public boolean updateOne(Investor investor){
    investor= repository.findById(investor.getUsername()).orElse(null);
    if(investor != null) {
      repository.save(investor);
      return true;
    }
      return false;
  }

  public HttpStatus delete(String pseudo){
    Investor investor = repository.findById(pseudo).orElse(null);
    if(investor != null){
      repository.deleteById(pseudo);

      return HttpStatus.OK;
    }
    return HttpStatus.NOT_FOUND;
  }
}
