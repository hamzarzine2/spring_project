package com.example.execution.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "price", url = "http://localhost:9008")
public interface PriceProxy {
    @PatchMapping("/price/{ticker}")
    ResponseEntity<Void> updatePrice(@PathVariable String ticker, @RequestBody long price);
}
