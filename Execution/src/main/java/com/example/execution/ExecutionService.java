package com.example.execution;

import com.example.execution.model.Position;
import com.example.execution.model.Transaction;
import com.example.execution.proxy.OrderProxy;
import com.example.execution.proxy.PriceProxy;
import com.example.execution.proxy.WalletProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

    @Autowired
    private WalletProxy walletProxy;

    @Autowired
    private PriceProxy priceProxy;

    @Autowired
    private OrderProxy orderProxy;

    public boolean executeOrder(String ticker, String seller, String buyer, Transaction transaction) {
        return executeUpdateStock(transaction, seller, "CASH",transaction.getQuantity()) &&
                executeUpdateStock(transaction, buyer, "CASH", -transaction.getQuantity()) &&
                executeUpdateStock(transaction, seller, ticker,-transaction.getQuantity()) &&
                executeUpdateStock(transaction, buyer, ticker,-transaction.getQuantity()) &&
                handleResponse(priceProxy.updatePrice(ticker, transaction.getPrice())) &&
                handleResponse(updateOrder(transaction.getBuy_order_guid(), transaction.getQuantity())) &&
                handleResponse(updateOrder(transaction.getSell_order_guid(), transaction.getQuantity()));
    }

    private boolean executeUpdateStock(Transaction transaction, String user, String ticker,int montant ) {
        Position position = new Position();
        position.setTicker(ticker);
        //problème de moins ici
        position.setQuantity(montant);
        position.setUnitValue(transaction.getPrice());
        ResponseEntity<Void> walletResponse = walletProxy.addPosition(user, position);
        return handleResponse(walletResponse);
    }

    private boolean handleResponse(ResponseEntity<Void> response) {
        return !response.getStatusCode().isError();
    }

    private ResponseEntity<Void> updateOrder(String orderId, int quantity) {
        return orderProxy.updateOrder(orderId, quantity);
    }
}
