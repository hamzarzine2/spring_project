package com.example.execution.proxy;

import com.example.execution.model.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@FeignClient(name = "wallets", url = "http://localhost:9007")
public interface WalletProxy {
    @PostMapping("/wallet/{username}")
    ResponseEntity<List<Position>> addPosition(@PathVariable String username, @RequestBody List<Position> position) ;



}
