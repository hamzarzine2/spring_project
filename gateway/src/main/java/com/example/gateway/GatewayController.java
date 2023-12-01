package com.example.gateway;

import com.example.gateway.exceptions.BadRequestException;
import com.example.gateway.exceptions.ConflictException;
import com.example.gateway.exceptions.NotFoundException;
import com.example.gateway.exceptions.UnauthorizedException;
import com.example.gateway.models.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class GatewayController {
  private final GatewayService service;

  public GatewayController(GatewayService service) {
    this.service = service;
  }

  @GetMapping("/investor/{username}")
  public ResponseEntity<Investor> readInvestor(@PathVariable String username) {
    Investor investor;
    try {
      investor = service.readInvestor(username);
      if (investor == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      return new ResponseEntity<>(investor, HttpStatus.OK);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/investor/{username}")
  public ResponseEntity<Void> createInvestor(@PathVariable String username,
                                             @RequestBody InvestorWithCredentials investor) {

    if (!Objects.equals(investor.getInvestor_data().getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    try {
      service.createInvestor(investor);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (ConflictException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @PutMapping("/investor/{username}")
  public ResponseEntity<Void> updateInvestor(@PathVariable String username,
                                             @RequestBody Investor investor,
                                             @RequestHeader("Authorization") String token) {
    if (!Objects.equals(investor.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    String userPseudo = service.verify(token);
    if (userPseudo == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    try {
      service.updateInvestor(investor);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException | NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/investor/{username}")
  public ResponseEntity<Void> deleteInvestor(@PathVariable String username,
                                             @RequestHeader("Authorization") String token) {
    String user = service.verify(token);
    if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    else if (!Objects.equals(user, username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    boolean found = service.deleteInvestor(username);

    if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    else return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/authentication/connect")
  public ResponseEntity<String> connect(@RequestBody Credentials credentials) {
    try {
      String token = service.connect(credentials);
      return new ResponseEntity<>(token, HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PutMapping("/authentication/{username}")
  public ResponseEntity<Void> updateCredentials(@PathVariable String username,
                                                @RequestBody Credentials credentials) {
    try {
      service.updateCredentials(username, credentials);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/order")
  public ResponseEntity<Order> makeAnOrder(@RequestBody Order order) {
    try {
      service.placeAnOrder(order);
      return new ResponseEntity<>(order, HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/order/by-user/{username}")
  public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String username,
                                                         @RequestHeader("Authorization") String token) {
    List<Order> orders;

    try {
      String user = service.verify(token);
      if (!user.equals(username)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      orders = service.getOrderByUsername(username);
      if (orders == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      return new ResponseEntity<>(orders, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  @GetMapping("/wallet/{username}")
  public ResponseEntity<Iterable<Wallet>> getOpenPositions(@PathVariable String username,
                                                           @RequestHeader("Authorization") String token) {
    Investor investor;
    try {
      String user = service.verify(token);
      if (!user.equals(username)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      investor = service.readInvestor(username);
      if (investor == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      Iterable<Wallet> openPositions = service.getOpenPositions(username);
      return new ResponseEntity<>(openPositions, HttpStatus.OK);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/wallet/{username}/cash")
  public ResponseEntity<Iterable<Wallet>> updateCash(@PathVariable String username,
                                                     @RequestBody double cash,
                                                     @RequestHeader("Authorization") String token) {
    Investor investor;
    try {
      String user = service.verify(token);
      if (!user.equals(username)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      investor = service.readInvestor(username);
      if (investor == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      Iterable<Wallet> updatedWallet = service.updateCashWallet(username, cash);
      return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/wallet/{username}/net-worth")
  public ResponseEntity<Double> totalWallet(@PathVariable String username,
                                            @RequestHeader("Authorization") String token) {
    Investor investor;
    try {
      String user = service.verify(token);
      if (!user.equals(username)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      investor = service.readInvestor(username);
      if (investor == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      double total = service.walletValue(username);
      return new ResponseEntity<>(total, HttpStatus.OK);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/wallet/{username}/position/{ticker}")
  public ResponseEntity<Iterable<Wallet>> editTitle(@PathVariable String username,
                                                    @PathVariable String ticker,
                                                    @RequestBody double quantity,
                                                    @RequestHeader("Authorization") String token) {
    Investor investor;
    try {
      String user = service.verify(token);
      if (!user.equals(username)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      investor = service.readInvestor(username);
      if (investor == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      double amount = service.updateTitle(username, ticker, quantity);
      Iterable<Wallet> updatedWallet = service.updateCashWallet(username, amount);
      return new ResponseEntity<>(updatedWallet, HttpStatus.OK);

    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
