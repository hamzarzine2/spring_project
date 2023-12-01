package com.example.gateway.data;

import com.example.gateway.models.Wallet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "wallet")
public interface WalletProxy {

  @GetMapping("/wallet/{username}")
  Iterable<Wallet> getOpenPositions(@PathVariable String username);

  @PostMapping("/wallet/{username}/cash")
  Iterable<Wallet> updateCashWallet(@PathVariable String username, @RequestBody double cash);

  @GetMapping("/wallet/{username}/net-worth")
  double walletValue(@PathVariable String username);

  @PostMapping("/wallet/{username}/position/{ticker}")
  double updateTitle(@PathVariable String username, @PathVariable String ticker, @RequestBody double quantity);
}
