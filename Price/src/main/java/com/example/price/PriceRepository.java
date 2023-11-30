package com.example.price;

import com.example.price.models.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, String> {

  int findByPrice(String ticker);
}
