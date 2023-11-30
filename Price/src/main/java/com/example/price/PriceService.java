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
  public double getLastPrice(String ticker) {
    Price price = repository.findPriceByTicker(ticker);
    if (price == null) return -1;
    return price.getPrice();
  }

  public double updatePrice(double newPrice, String ticker){
    repository.updatePriceByTicker(ticker, newPrice);
    return newPrice;
  }

}
