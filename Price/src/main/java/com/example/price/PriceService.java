package com.example.price;

import org.springframework.stereotype.Service;

@Service
public class PriceService {

  private final PriceRepository repository;

  public PriceService(PriceRepository priceRepository) {
    this.repository = priceRepository;
  }

  /**
   * Get the last price of given ticker
   * @param ticker
   * @return the price
   */
  public int getLastPrice(String ticker) {
    return repository.findByPrice(ticker);
  }

  public int updatePrice(int newPrice, String ticker){
    if (newPrice < 0) return -1;
    int price = repository.findByPrice(ticker);
    return newPrice;
  }

}
