package com.example.order;

import com.example.order.Model.Order;
import com.example.order.Model.Side;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOne(Order order) {
        return orderRepository.save(order);
    }

    public Order readOne(String guid) {
        return orderRepository.findById(guid).orElse(null);
    }

    public boolean updateOne(Order order) {
        Order orderToChange = orderRepository.findById(order.getId()).orElse(null);
        if (orderToChange==null) return false;
        orderToChange.setFilled(order.getFilled());
        orderRepository.save(orderToChange);
        return true;
    }

    public List<Order> getAllOrderByUser(String username) {
        return orderRepository.findAllByOwner(username);
    }

    public List<Order> getOrderByTicketNotClose(String ticker, Side side) {
        return orderRepository.findNotDoneTicker(ticker,side);
    }
}
