package com.example.order;

import com.example.order.Model.Order;
import com.example.order.Model.Side;
import jakarta.ws.rs.QueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOne(@RequestBody Order order) {
        if (order.getOwner().isEmpty() || order.getTimestamp() <= 0 || order.getTicker().isEmpty()
                || order.getQuantity() <= 0 || order.getLimit() < 0 || order.getFilled() < 0
        ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(orderService.createOne(order), HttpStatus.CREATED);
    }

    @GetMapping("/order/{guid}")
    public ResponseEntity<Order> readOne(@PathVariable("guid") String guid) {
        Order order = orderService.readOne(guid);
        if (order == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/order/{guid}")
    public ResponseEntity<Order> updateOne(@PathVariable("guid") String guid, @RequestBody Order orderValue) {
        int filledValue = orderValue.getFilled();
        if(filledValue<0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return orderService.updateOne(guid,filledValue) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/order/by-user/{username}")
    public ResponseEntity<List<Order>> getAllOrderByUser(@PathVariable("username") String username) {
        List<Order> orders = orderService.getAllOrderByUser(username);
        if (orders.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/order/open/by-ticker/{ticker}/{side}")
    public ResponseEntity<List<Order>> getOrderByTicketNotClose(@PathVariable("ticker") String ticker, @PathVariable("side") Side side) {
        List<Order> orders = orderService.getOrderByTicketNotClose(ticker,side);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
