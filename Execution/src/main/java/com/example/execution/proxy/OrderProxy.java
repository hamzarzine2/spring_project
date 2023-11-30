package com.example.execution.proxy;

import com.example.execution.model.Order;
import com.example.execution.model.Position;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@FeignClient(name = "order")
public interface OrderProxy {

    @PatchMapping("/order/{guid}")
    ResponseEntity<Order> updateOrder(@PathVariable String guid, @RequestBody int filledQuantity);


}
