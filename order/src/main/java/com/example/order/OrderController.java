package com.example.order;

import com.example.order.Model.Order;
import jakarta.ws.rs.QueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(guid);
        Order order = orderService.readOne(guid);
        if (order == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/order/{guid}")
    public ResponseEntity<Order> updateOne(@PathVariable("guid") String guid, @RequestBody Order order) {
        if(order.getFilled()<0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        order.setId(guid);
        boolean updated = orderService.updateOne(order);
        if(!updated) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
