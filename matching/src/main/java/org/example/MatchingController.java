package org.example;

import com.example.order.Model.Order;
import com.example.order.Model.Side;
import java.util.List;
import org.example.proxies.ExecutionProxy;
import org.example.proxies.OrderProxy;
import org.example.proxies.PriceProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingController {
    private final ExecutionProxy executionProxy;
    private final OrderProxy orderProxy;
    private final PriceProxy priceProxy;
    private final MatchingService service;

    public MatchingController(ExecutionProxy executionProxy, OrderProxy orderProxy, PriceProxy priceProxy) {
        this.executionProxy = executionProxy;
        this.orderProxy = orderProxy;
        this.priceProxy = priceProxy;
        this.service = new MatchingService(executionProxy);
    }

    /**
     * Matches buy orders with sell orders for a specific ticker.
     * @param ticker The ticker of the orders to match.
     * @return  HTTP status code 200 (OK) if matching is successful.
     *          HTTP status code 400 (BAD REQUEST) if ticker is missing or empty.
     *          HTTP status code 500 (INTERNAL SERVER ERROR) if matching fails.
     */
    @PostMapping("/trigger/{ticker}")
    public ResponseEntity<?> trigger(@PathVariable String ticker) {
        if(ticker == null || ticker.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Order> buyOrders = orderProxy.getOrders(ticker, Side.BUY).getBody();
        List<Order> sellOrders = orderProxy.getOrders(ticker, Side.SELL).getBody();
        double price = priceProxy.getPrice(ticker).getBody();
        if(service.match(buyOrders, sellOrders, price) > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
