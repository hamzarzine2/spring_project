package com.example.wallet.repositories;

import com.example.wallet.Position;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Position, String>{

  @Query("select p from positions p")
  List<Position> getAllPositions();
}
