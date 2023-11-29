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
<<<<<<< Updated upstream
  Position getUserPosition(@Param("username") String username, @Param("ticker") String ticker);


  List<Position> getAllByUsernameEquals(String username);
=======
  Position getAllUserPositions(@Param("username") String username, @Param("ticker") String ticker);

  @Query("select sum(p.quantity*p.unitValue) from positions p where p.username =: username")
  double getNetWorth(@Param("username") String username);
>>>>>>> Stashed changes
}
