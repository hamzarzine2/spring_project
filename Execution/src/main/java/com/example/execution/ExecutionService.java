package com.example.execution;

import com.example.execution.model.Order;
import com.example.execution.model.Position;
import com.example.execution.model.Transaction;
import com.example.execution.proxy.OrderProxy;
import com.example.execution.proxy.PriceProxy;
import com.example.execution.proxy.WalletProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class handles the execution of orders, updates positions in the wallet, and communicates
 * with various proxies to handle transactions.
 *
 * @author hamza
 * @version 1.0
 */
@Service
public class ExecutionService {

    @Autowired
    private WalletProxy walletProxy;

    @Autowired
    private PriceProxy priceProxy;

    @Autowired
    private OrderProxy orderProxy;

    /**
     * Executes a transaction, updating the buyer, seller, and related entities.
     *
     * @param ticker      the ticker symbol of the transaction
     * @param seller      the seller's username
     * @param buyer       the buyer's username
     * @param transaction the transaction details
     * @return {@code true} if the execution is successful, {@code false} otherwise
     */
    public boolean executeOrder(String ticker, String seller, String buyer, Transaction transaction) {
        try {
            return  executeupdateBuyer(transaction,buyer) && executeupdateSeller(transaction,seller) &&
                    handleResponse(priceProxy.updatePrice(ticker, transaction.getPrice())) &&
                    updateOrder(transaction.getBuy_order_guid(), transaction.getQuantity()) &&
                    updateOrder(transaction.getSell_order_guid(), transaction.getQuantity());
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Executes updates for the seller's position and cash in the wallet microservice.
     *
     * @param transaction the transaction details
     * @param user        the seller's username
     * @return {@code true} if the execution is successful, {@code false} otherwise
     */
    private boolean executeupdateSeller(Transaction transaction, String user){
        List<Position> positionList = new ArrayList<>();
        Position positionTicket = new Position();
        positionTicket.setTicker(transaction.getTicker());
        positionTicket.setQuantity(- transaction.getQuantity());
        positionTicket.setUnitValue(transaction.getPrice());
        Position positionCash = createPositionCash(transaction.getPrice());

        positionList.add(positionTicket);
        positionList.add(positionCash);
        ResponseEntity<List<Position>> walletResponse = walletProxy.addPosition(user, positionList);
        System.out.println(walletResponse.getStatusCode());
        return true;
    }

    /**
     * Executes updates for the buyer's position and cash in the wallet microservice.
     *
     * @param transaction the transaction details
     * @param user        the buyer's username
     * @return {@code true} if the execution is successful, {@code false} otherwise
     */
    private boolean executeupdateBuyer(Transaction transaction, String user ){
        List<Position> positionList = new ArrayList<>();
        Position positionTicket = new Position();
        positionTicket.setTicker(transaction.getTicker());
        positionTicket.setQuantity(transaction.getQuantity());
        positionTicket.setUnitValue(transaction.getPrice());
        Position positionCash = createPositionCash(-transaction.getPrice());
        positionList.add(positionTicket);
        positionList.add(positionCash);
        ResponseEntity<List<Position>> walletResponse = walletProxy.addPosition(user, positionList);
        System.out.println(walletResponse.getStatusCode());
        return true;
    }

    /**
     * Creates a position representing cash.
     *
     * @param amount the amount of cash
     * @return a {@code Position} object representing cash
     */
    private Position createPositionCash(double amount){
        Position positionCash = new Position();
        positionCash.setTicker("CASH");
        positionCash.setQuantity(amount);
        positionCash.setUnitValue(1);
        return positionCash;
    }

    /**
     * Handles the response from a proxy, printing the status code and returning the success status.
     *
     * @param response the response entity from the proxy
     * @return {@code true} if the response indicates success, {@code false} otherwise
     */
    private boolean handleResponse(ResponseEntity<Void> response) {
        System.out.println(!response.getStatusCode().isError());
        return !response.getStatusCode().isError();
    }

    /**
     * Updates the specified order with the given quantity by calling the order microservice.
     *
     * @param orderId  the ID of the order to be updated
     * @param quantity the quantity to update the order with
     * @return {@code true} if the update is successful, {@code false} otherwise
     */
    private boolean updateOrder(String orderId, int quantity) {
        Order orderTemp = new Order();
        orderTemp.setFilled(quantity);
        return !orderProxy.updateOrder(orderId, orderTemp).getStatusCode().isError();
    }
}
