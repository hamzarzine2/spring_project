package com.example.execution.proxy;

import jakarta.ws.rs.QueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "order")
public interface OrderProxy {

    @PatchMapping("/order/{guid}")
    ResponseEntity<Void> updateOrder(@PathVariable String guid, @RequestBody int filledQuantity);
}
