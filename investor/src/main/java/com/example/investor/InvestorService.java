package com.example.investor;

import com.example.investor.model.UnSafeCredentials;
import com.example.investor.model.Order;
import com.example.investor.repository.AuthProxy;
import com.example.investor.repository.InvestorRepository;
import com.example.investor.model.Investor;
import com.example.investor.model.InvestorWithPassword;
import com.example.investor.repository.WalletProxy;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class InvestorService {
  private final InvestorRepository repository ;

  private final WalletProxy walletProxy;
  private final AuthProxy authProxy;

  public InvestorService(InvestorRepository repository, WalletProxy walletProxy,
      AuthProxy authProxy) {
    this.repository = repository;
    this.walletProxy = walletProxy;
    this.authProxy = authProxy;
  }

  public Investor getOne(String name){
   return repository.findById(name).orElse(null);
  }

  public boolean createOne(InvestorWithPassword investorWithPswrd){
    Investor investor= repository.findById(investorWithPswrd.getInvestor_data().getUsername()).orElse(null);
    if(investor == null) {
      investor=investorWithPswrd.getInvestor_data();
      UnSafeCredentials unSafeCredentials = new UnSafeCredentials(investor.getUsername(),investorWithPswrd.getPassword());
     ResponseEntity<Void> response =  authProxy.createCredential(investor.getUsername(),
         unSafeCredentials);
      if(response.getStatusCode().is2xxSuccessful()) {
        repository.save(investorWithPswrd.getInvestor_data());
        return true;
      }
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
      ResponseEntity<List<Order>>  response = walletProxy.getWallet(pseudo);
      if(response.getStatusCode().is2xxSuccessful()){
        ResponseEntity<Void> responseAuth = authProxy.deleteCredential(pseudo);
        if(responseAuth.getStatusCode().is2xxSuccessful()) {
          repository.deleteById(pseudo);
          return HttpStatus.OK;
        }
      }
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.NOT_FOUND;
  }
}
