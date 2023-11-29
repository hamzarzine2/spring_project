package com.example.wallet.repositories;

import com.example.wallet.models.Investor;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "investor")
public interface InvestorProxy {
  @GetMapping("/investor/{username}")
  Investor getInvestor(@PathVariable String username);
}
