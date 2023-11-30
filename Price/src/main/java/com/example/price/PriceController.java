package com.example.price;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PriceController {
  private PriceRepository repository;
  private PriceService service;

  @GetMapping("/price/{ticker}")
  public ResponseEntity<Integer> getLastPrice(@PathVariable String ticker){
    if (!repository.findById(ticker).isPresent())
      return new ResponseEntity<>(1, HttpStatus.OK);//TODO
    int lastPrice = service.getLastPrice(ticker);
    return new ResponseEntity<>(lastPrice, HttpStatus.OK);
  }

  @PatchMapping("/price/{ticker}")
  public ResponseEntity<Integer> updatePrice(@PathVariable String ticker, @RequestBody int newPrice){
    System.out.println(newPrice);
    if (!repository.findById(ticker).isPresent())
      return new ResponseEntity<>(1, HttpStatus.OK);//TODO
    if (service.updatePrice(newPrice, ticker) == -1)
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(newPrice, HttpStatus.OK);
  }
}
