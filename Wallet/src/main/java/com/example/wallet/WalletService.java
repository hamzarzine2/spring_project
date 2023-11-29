package com.example.wallet;

import com.example.wallet.models.Position;
import com.example.wallet.models.PositionDTO;
import com.example.wallet.repositories.InvestorProxy;
import com.example.wallet.repositories.WalletRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private final WalletRepository repository;
  private final InvestorProxy investorproxy;

  private final PositionDTO positionDTO = new PositionDTO();

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
   * @param positionDTOs containing the positions to add AND the price in CASH to remove from the wallet
   *                  concerning the price of the positions
   * @return the content of the wallet after the operation
   */
  public List<PositionDTO> addPosition(String username, List<PositionDTO> positionDTOs){
    if (positionDTOs == null || positionDTOs.isEmpty())
      return null;
    List<PositionDTO> positionList = null;
    for (PositionDTO dto : positionDTOs) {
      Position position = repository.getAllUserPositions(username, dto.getTicker());
      if(position == null){
        position = new Position();
        position.setUsername(username);
        position.setTicker(dto.getTicker());
        position.setQuantity(dto.getQuantity());
        position.setUnitValue(dto.getUnitValue());
      } else {
        position.setQuantity(position.getQuantity() + dto.getQuantity());
      }
      repository.save(position);
      positionList.add(positionDTO.toDto(position));
    }
    return positionList;
  }
}
