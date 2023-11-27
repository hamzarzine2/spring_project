package com.example.matching;

import com.example.matching.Model.Order;
import com.example.matching.Model.Side;
import com.example.matching.Model.Type;
import java.util.List;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.web.client.RestTemplate;

public class MatchingService {
  private static final RestTemplate REST_TEMPLATE = new RestTemplate();
  private static final String BASE_URL = "http://localhost";

  //ports unknown at time of writing this code, will complete later.
  private static final int PRICE_PORT = 0;
  private static final int EXECUTE_PORT = 0;


  /**
   * Matches BUY orders with SELL orders
   * @param ticker the stock to match
   * @return the number of stocks traded
   */
  public int trigger(String ticker) {
    int sold = 0;
    double marketPrice = REST_TEMPLATE.getForObject(BASE_URL + ":" + PRICE_PORT + "/price/" + ticker, Integer.class);
    String url = BASE_URL + ":9005/order/open/by-ticker/" + ticker + "/";
    List<Order> buyOrders = REST_TEMPLATE.getForObject(url + Side.BUY, List.class);
    List<Order> sellOrders = REST_TEMPLATE.getForObject(url + Side.SELL, List.class);
    for (Order buy : buyOrders) {
      int toFill = buy.getQuantity() - buy.getFilled();
      for (Order sell : sellOrders) {
        int available = sell.getQuantity() - sell.getFilled();

        if (buy.getType().equals(Type.LIMIT) && sell.getType().equals(Type.LIMIT) ) {
          if(buy.getLimit() >= sell.getLimit()) {
            toFill -= available;
            sold += available;
            REST_TEMPLATE.postForLocation(BASE_URL + ":" + EXECUTE_PORT + "/execute/" + ticker + "/" + buy.getOwner() + "/" + sell.getOwner(), "");
          }
        }
        else if (buy.getType().equals(Type.MARKET) && sell.getType().equals(Type.LIMIT) ) {
          if(marketPrice >= sell.getLimit()) {
            toFill -= available;
            sold += available;
            REST_TEMPLATE.postForLocation(BASE_URL + ":" + EXECUTE_PORT + "/execute/" + ticker + "/" + buy.getOwner() + "/" + sell.getOwner(), "");
          }
        }
        else if (buy.getType().equals(Type.LIMIT) && sell.getType().equals(Type.MARKET) ) {
          if(buy.getLimit() >= marketPrice) {
            toFill -= available;
            sold += available;
            REST_TEMPLATE.postForLocation(BASE_URL + ":" + EXECUTE_PORT + "/execute/" + ticker + "/" + buy.getOwner() + "/" + sell.getOwner(), "");
          }
        }
        else if (buy.getType().equals(Type.MARKET) && sell.getType().equals(Type.MARKET) ) {
          toFill -= available;
          sold += available;
          REST_TEMPLATE.postForLocation(BASE_URL + ":" + EXECUTE_PORT + "/execute/" + ticker + "/" + buy.getOwner() + "/" + sell.getOwner(), "");
        }
      }
    }


    return sold;
  }

}
