package com.example.wallet;

import com.example.wallet.repositories.WalletRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private final WalletRepository repository;

  public WalletService(WalletRepository walletRepository) {
    this.repository = walletRepository;
  }

  /**
   * Get the net worth of the user's wallet
   * @param username of the user
   * @return the wallet's net worth
   */
  public double getNetWorth(String username) {
    //todo verify that the user exists

    double netWorth = 0;
    for (Position position : repository.findAll()) {
      netWorth += position.getNetWorth();
    }
    return netWorth;
  }

  /**
   * Get the list of all the opened positions of the user
   * @param username of the user
   * @return a list of opened positions
   */
  public List<Position> getOpenedPositions(String username){
    //todo verify that the user exists

    List<Position> positions = null;
    for (Position position : repository.findAll()) {
      if(position.getNetWorth() > 0){
        positions.add(position);
      }
    }
    return positions;
  }

  //todo add position method
}
