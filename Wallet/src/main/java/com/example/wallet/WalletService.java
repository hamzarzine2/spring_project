package com.example.wallet;

import com.example.wallet.repositories.InvestorProxy;
import com.example.wallet.repositories.WalletRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private final WalletRepository repository;
  private final InvestorProxy investorproxy;

  public WalletService(WalletRepository walletRepository, InvestorProxy investorproxy) {
    this.repository = walletRepository;
    this.investorproxy = investorproxy;
  }

  /**
   * Get the net worth of the user's wallet
   * @param username of the user
   * @return the wallet's net worth
   */
  public double getNetWorth(String username) {
    double netWorth = 0;
    if(investorproxy.getInvestor(username) == null) //TODO
      return 0;

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
    List<Position> positions = null;
    if (investorproxy.getInvestor(username) == null)
      return null; //TODO
    for (Position position : repository.findAll()) {
      if(position.getNetWorth() > 0){
        positions.add(position);
      }
    }
    return positions;
  }

  /**
   * Add one or more positions to the user's wallet.
   * @param username of the user
   * @param positions containing the positions to add AND the price in CASH to remove from the wallet
   *                  concerning the price of the positions
   * @return the content of the wallet after the operation
   */
  public List<Position> addPosition(String username, List<Position> positions){
    if (positions == null || positions.isEmpty())
      return null;
    List<Position> positionList = repository.getAllPositions();

    for (Position position : positions) {
      Position existingPosition = repository.findById(position.getTicker()).orElse(null);

      if (existingPosition == null) {
        repository.save(position);
        positionList.add(position);
      } else {
        existingPosition.setQuantity(position.getQuantity() + existingPosition.getQuantity());
        repository.save(existingPosition);
        positionList.add(existingPosition);
      }
    }
    return positionList;
  }
}
