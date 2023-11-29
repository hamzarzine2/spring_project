package com.example.wallet.repositories;

import com.example.wallet.models.Position;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Position, String>{

  Position getPositionsByUsernameAndTicker(String username, String ticker);

  List<Position> getAllByUsernameEquals(String username);
}
