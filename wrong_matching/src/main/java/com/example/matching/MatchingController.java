package com.example.matching;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public class MatchingController {
  private final MatchingService service;

  public MatchingController(MatchingService service) {
    this.service = service;
  }

  /**
   * Matches new order with previous ones to complete them.
   * @param ticker The identifier of the order.
   * @return HTTP status 200 if everything went well
   * @return HTTP status 400 if ticker is missing or empty
   * @return HTTP status 404 if the order can't be found
   */
  @PostMapping("/trigger/{ticker}")
  public ResponseEntity<?> trigger(@PathVariable("ticker") String ticker) {
    if(ticker == null || ticker.isBlank()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    if(service.trigger(ticker) < 0) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    return new ResponseEntity(HttpStatus.OK);
  }

}
