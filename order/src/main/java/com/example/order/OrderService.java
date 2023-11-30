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

    public boolean updateOne(String guid, int filled) {
        Order order = readOne(guid);
        if (order == null || order.getFilled()+filled>order.getQuantity()) return false;
        order.setFilled(order.getFilled() + filled);
        orderRepository.save(order);
        return true;
    }

    public List<Order> getAllOrderByUser(String username) {
        return orderRepository.findAllByOwner(username);
    }

    public List<Order> getOrderByTicketNotClose(String ticker, Side side) {
        return orderRepository.findNotDoneTicker(ticker,side);
    }
}
