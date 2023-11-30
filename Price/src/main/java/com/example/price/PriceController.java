package com.example.price;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {
  private final PriceService service;

  public PriceController(PriceService priceService){
    this.service = priceService;
  }

  @GetMapping("/price/{ticker}")
  public ResponseEntity<Double> getLastPrice(@PathVariable String ticker){
    double lastPrice = service.getLastPrice(ticker);
    if (lastPrice == -1)
      return new ResponseEntity<>(1.0, HttpStatus.OK);
    return new ResponseEntity<>(lastPrice, HttpStatus.OK);
  }

  @PatchMapping("/price/{ticker}")
  public ResponseEntity<Double> updatePrice(@PathVariable String ticker, @RequestBody double newPrice) {
    if (newPrice < 0)
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    service.updatePrice(newPrice, ticker);
    return new ResponseEntity<>(newPrice, HttpStatus.OK);
  }
}
