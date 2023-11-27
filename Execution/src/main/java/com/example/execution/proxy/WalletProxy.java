package com.example.execution.proxy;

import com.example.execution.model.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "wallet")
public interface WalletProxy {
    @PostMapping("/wallet/{username}")
    ResponseEntity<Void> addPosition(@PathVariable String username, @RequestBody Position position) ;



}
