package com.example.investor;

import com.example.investor.model.PositionDTO;
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


/**
 * Service class for managing Investor entities.
 */
@Service
public class InvestorService {

  // Fields

  /**
   * The repository for accessing Investor entities.
   */
  private final InvestorRepository repository;

  /**
   * The proxy for interacting with the Wallet service.
   */
  private final WalletProxy walletProxy;

  /**
   * The proxy for interacting with the authentication service.
   */
  private final AuthProxy authProxy;

  // Constructor

  /**
   * Constructs an InvestorService with the specified repositories and proxies.
   *
   * @param repository   The repository for accessing Investor entities.
   * @param walletProxy  The proxy for interacting with the Wallet service.
   * @param authProxy    The proxy for interacting with the authentication service.
   */
  public InvestorService(InvestorRepository repository, WalletProxy walletProxy,
      AuthProxy authProxy) {
    this.repository = repository;
    this.walletProxy = walletProxy;
    this.authProxy = authProxy;
  }

  // Methods

  /**
   * Retrieves an Investor by their name.
   *
   * @param name The name of the Investor to retrieve.
   * @return The Investor with the specified name, or null if not found.
   */
  public Investor getOne(String name) {
    return repository.findById(name).orElse(null);
  }

  /**
   * Retrieves all Investors.
   *
   * @return A list of all Investors.
   */
  public List<Investor> getALL() {
    return (List<Investor>) repository.findAll();
  }

  /**
   * Creates a new Investor with the provided data and credentials.
   *
   * @param investorWithPswrd The Investor data along with the password.
   * @return true if the Investor is successfully created, false otherwise.
   */
  public boolean createOne(InvestorWithPassword investorWithPswrd) {
    Investor investor = repository.findById(investorWithPswrd.getInvestor_data().getUsername()).orElse(null);
    if (investor == null) {
      investor = investorWithPswrd.getInvestor_data();
      UnSafeCredentials unSafeCredentials = new UnSafeCredentials(investor.getUsername(), investorWithPswrd.getPassword());
      ResponseEntity<Void> response = authProxy.createCredential(investor.getUsername(),
          unSafeCredentials);
      if (response.getStatusCode().is2xxSuccessful()) {
        repository.save(investorWithPswrd.getInvestor_data());
        return true;
      }
    }
    return false;
  }

  /**
   * Updates an existing Investor.
   *
   * @param investor The Investor to be updated.
   * @return true if the Investor is successfully updated, false otherwise.
   */
  public boolean updateOne(Investor investor) {
    investor = repository.findById(investor.getUsername()).orElse(null);
    if (investor != null) {
      repository.save(investor);
      return true;
    }
    return false;
  }

  /**
   * Deletes an Investor by their pseudo (username).
   *
   * @param pseudo The pseudo of the Investor to be deleted.
   * @return HttpStatus.OK if the deletion is successful, HttpStatus.BAD_REQUEST if there
   * are issues with Wallet or authentication, HttpStatus.NOT_FOUND if the Investor is not found.
   */
  public HttpStatus delete(String pseudo) {
    Investor investor = repository.findById(pseudo).orElse(null);
    if (investor != null) {
      ResponseEntity<List<PositionDTO>> response = walletProxy.getWallet(pseudo);
      if (response.getStatusCode().is2xxSuccessful()) {
        ResponseEntity<Void> responseAuth = authProxy.deleteCredential(pseudo);
        if (responseAuth.getStatusCode().is2xxSuccessful()) {
          repository.deleteById(pseudo);
          return HttpStatus.OK;
        }
      }
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.NOT_FOUND;
  }
}
