package com.example.wallet;

import com.example.wallet.models.Position;
import com.example.wallet.models.PositionDTO;
import com.example.wallet.repositories.WalletRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private final WalletRepository repository;
  private final PositionDTO positionDTO = new PositionDTO();

  public WalletService(WalletRepository walletRepository) {
    this.repository = walletRepository;
  }

  /**
   * Get the net worth of the user's wallet
   * @param username of the user
   * @return the wallet's net worth
   */
  public double getNetWorth(String username) {
    double netWorth = 0;
    List<Position> positions = repository.getAllByUsernameEquals(username);
    for (Position position : positions) {
      netWorth += position.getQuantity() * position.getUnitValue();
    }
    return netWorth;
  }

  /**
   * Get the list of all the opened positions of the user
   * @param username of the user
   * @return a list of opened positions
   */
  public List<PositionDTO> getOpenedPositions(String username){
    List<Position> positions = repository.getAllByUsernameEquals(username);
    List<PositionDTO> positionDTOs = new ArrayList<>();

    for (Position position : positions) {
      PositionDTO currentPosition = new PositionDTO();
      currentPosition.toDto(position);
      positionDTOs.add(currentPosition);
    }
    return positionDTOs;
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

    List<PositionDTO> positionList = new ArrayList<>();
    for (PositionDTO dto : positionDTOs) {
      Position position = repository.getPositionsByUsernameAndTicker(username, dto.getTicker());
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
      PositionDTO currentPosition = new PositionDTO();
      currentPosition.toDto(position);
      positionList.add(currentPosition);
    }
    System.out.println("exit addPosition");
    return positionList;
  }
}
