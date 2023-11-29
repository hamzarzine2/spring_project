package com.example.gateway.data;

import com.example.gateway.models.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "OrderProxy")
public interface OrderProxy {


  @PostMapping("/order")
  void makeAnOrder(@RequestBody Order order);

  @GetMapping("/order/by-user/{username}")
  Iterable<Order> getOrdersFromInvestor(@PathVariable String username);

  @DeleteMapping("/order/{username}")
  void deleteOrdersFromInvestor(@PathVariable String username);
}
