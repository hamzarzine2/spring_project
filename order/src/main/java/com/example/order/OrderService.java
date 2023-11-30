package com.example.order;

import com.example.order.Model.Order;
import com.example.order.Model.Side;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class handles operations related to orders, such as creation, reading, updating,
 * and retrieving orders based on different criteria.
 *
 * @author hamza
 * @version 1.0
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * Constructs an {@code OrderService} with the specified {@code OrderRepository}.
     *
     * @param orderRepository the repository for orders
     */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new order and saves it using the {@code OrderRepository}.
     *
     * @param order the order to be created
     * @return the created order
     */
    public Order createOne(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Retrieves an order based on its GUID (Global Unique Identifier).
     *
     * @param guid the GUID of the order to be retrieved
     * @return the order with the specified GUID, or {@code null} if not found
     */
    public Order readOne(String guid) {
        return orderRepository.findById(guid).orElse(null);
    }

    /**
     * Updates the filled quantity of an order based on its GUID.
     *
     * @param guid   the GUID of the order to be updated
     * @param filled the quantity to be added to the filled quantity of the order
     * @return {@code true} if the update is successful, {@code false} otherwise
     */
    public boolean updateOne(String guid, int filled) {
        Order order = readOne(guid);
        if (order == null || order.getFilled()+filled>order.getQuantity()) return false;
        order.setFilled(order.getFilled() + filled);
        orderRepository.save(order);
        return true;
    }

    /**
     * Retrieves all orders owned by a specific user.
     *
     * @param username the username of the owner
     * @return a list of orders owned by the specified user
     */
    public List<Order> getAllOrderByUser(String username) {
        return orderRepository.findAllByOwner(username);
    }

    /**
     * Retrieves orders for a specific ticker that are not closed, based on the specified side.
     *
     * @param ticker the ticker symbol of the orders to be retrieved
     * @param side   the side (buy/sell) of the orders to be retrieved
     * @return a list of orders for the specified ticker and side that are not closed
     */
    public List<Order> getOrderByTicketNotClose(String ticker, Side side) {
        return orderRepository.findNotDoneTicker(ticker,side);
    }
}
