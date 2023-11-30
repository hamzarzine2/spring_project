package com.example.price;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PriceRepository extends CrudRepository<Price, String> {
  Price findPriceByTicker(String ticker);

  @Modifying
  @Transactional
  @Query("UPDATE prices p SET p.price = :newPrice WHERE p.ticker = :ticker")
  void updatePriceByTicker(String ticker, double newPrice);
}
