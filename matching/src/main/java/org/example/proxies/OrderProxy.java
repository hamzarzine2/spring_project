package org.example.proxies;

import com.example.order.Model.Order;
import com.example.order.Model.Side;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient
@Repository
public interface OrderProxy {
  @GetMapping("/order/open/by-ticker/{ticker}/{side}")
  ResponseEntity<List<Order>> getOrders(@PathVariable String ticker, @PathVariable Side side);
}
