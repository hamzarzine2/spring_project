package com.example.investor.repository;

import com.example.investor.model.Order;
import com.example.investor.model.PositionDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "wallet")
public interface WalletProxy {

  @GetMapping("/wallet/{username}")
  ResponseEntity<List<PositionDTO>> getWallet(@PathVariable String username);

}
