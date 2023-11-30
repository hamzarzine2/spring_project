package com.example.price;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {
  private final PriceService service;

  public PriceController(PriceService priceService){
    this.service = priceService;
  }

  @GetMapping("/price/{ticker}")
  public ResponseEntity<Integer> getLastPrice(@PathVariable String ticker){
    int lastPrice = service.getLastPrice(ticker);
    return new ResponseEntity<>(lastPrice, HttpStatus.OK);
  }

  @PatchMapping("/price/{ticker}")
  public ResponseEntity<Integer> updatePrice(@PathVariable String ticker, int newPrice){
    if (service.updatePrice(newPrice, ticker) == -1)
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(newPrice, HttpStatus.OK);
  }
}
