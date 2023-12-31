package com.example.gateway.data;

import com.example.gateway.models.Investor;
import com.example.gateway.models.InvestorWithCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "investor")
public interface InvestorProxy {
    @GetMapping("/investor/{username}")
    Investor readInvestor(@PathVariable String username);
    @PostMapping("/investor/{username}")
    ResponseEntity<Investor> createInvestor(@PathVariable String username, @RequestBody InvestorWithCredentials investor);
    @PutMapping("/investor/{username}")
    void updateInvestor(@PathVariable String username, @RequestBody Investor investor);
    @DeleteMapping("/investor/{username}")
    void deleteInvestor(@PathVariable String username);
}
