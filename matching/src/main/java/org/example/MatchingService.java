package org.example;

import com.example.order.Model.Order;
import com.example.order.Model.Type;
import java.util.List;
import org.example.proxies.ExecutionProxy;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {
  private final ExecutionProxy executionProxy;
  public MatchingService(ExecutionProxy executionProxy){
    this.executionProxy = executionProxy;
  }

  /**
   * matches buy orders with sell orders.
   * @param buyOrders A list of buy orders for a specific ticker.
   * @param sellOrders A list of sell orders for a specific ticker.
   * @param price The market price for that ticker.
   * @return the number of actions traded or -1 in case of errors.
   */
  public int match(List<Order> buyOrders, List<Order> sellOrders, double price) {
    int traded = 0;

    for (Order buy:buyOrders) {

      int toBuy = buy.getQuantity() - buy.getFilled();

      for (Order sell:sellOrders) {

        int toSell = sell.getQuantity() - sell.getFilled();

        switch((buy.getType() == Type.LIMIT?"LIMIT":"MARKET") + "-" + (sell.getType() == Type.LIMIT?"LIMIT":"MARKET")) {
          case "LIMIT-LIMIT":
            if(buy.getLimit() <= sell.getLimit()) {
              traded += toBuy >= toSell?toSell:toBuy;
              executionProxy.execute(buy.getTicker(), sell.getOwner(), buy.getOwner());
            }
            break;
          case "LIMIT-MARKET":
            if(buy.getLimit() >= price) {
              traded += toBuy >= toSell?toSell:toBuy;
              executionProxy.execute(buy.getTicker(), sell.getOwner(), buy.getOwner());
            }
            break;
          case "MARKET-LIMIT":
            if(sell.getLimit() <= price) {
              traded += toBuy >= toSell?toSell:toBuy;
              executionProxy.execute(buy.getTicker(), sell.getOwner(), buy.getOwner());
            }
            break;
          case "MARKET-MARKET":
            traded += toBuy >= toSell?toSell:toBuy;
            executionProxy.execute(buy.getTicker(), sell.getOwner(), buy.getOwner());
            break;
          default:
            return -1;
        }
      }
    }

    return traded;
  }

}
