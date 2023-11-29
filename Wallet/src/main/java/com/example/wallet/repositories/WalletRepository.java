package com.example.wallet.repositories;

import com.example.wallet.models.Position;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Position, String>{

  @Query("select p from positions p where p.username =: username and p.ticker =: ticker ")
  Position getUserPosition(@Param("username") String username, @Param("ticker") String ticker);

  @Query("select p from positions p where p.username =: username")
  List<Position> getAllUserPositions(@Param("username")String username);
}
