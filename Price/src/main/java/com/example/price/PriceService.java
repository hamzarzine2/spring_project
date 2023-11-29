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
    //todo verify that the ticket exists

    return repository.findById(ticker).get().getPrice();
  }

  public int updatePrice(int newPrice, String ticker){
    if (newPrice < 0) return -1;
    int price = repository.findById(ticker).get().getPrice();
    repository.findById(ticker).get().setPrice(price);
    return newPrice;
  }

}
