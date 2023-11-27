package com.example.execution;

import com.example.execution.model.Position;
import com.example.execution.model.Transaction;
import com.example.execution.proxy.OrderProxy;
import com.example.execution.proxy.PriceProxy;
import com.example.execution.proxy.WalletProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ExecutionService {

    @Autowired
    WalletProxy walletProxy;
    @Autowired
    PriceProxy priceProxy;
    @Autowired
    OrderProxy orderProxy;
    public boolean executeOrder( String ticker,  String seller,  String buyer,  Transaction transaction) {
        ResponseEntity<Void> walletSellerCashResponse= updateStock(transaction,seller,"CASH");
        if(!handleResponse(walletSellerCashResponse))return false;
        ResponseEntity<Void> walletCashBuy = updateStock(transaction,buyer,"CASH");
        if(!handleResponse(walletCashBuy))return false;
        ResponseEntity<Void> walletSellerStockResponse=updateStock(transaction,seller,ticker);
        if(!handleResponse(walletSellerStockResponse))return false;
        ResponseEntity<Void> walletBuyerStockResponse=updateStock(transaction,buyer,ticker);
        if(!handleResponse(walletBuyerStockResponse))return false;
        ResponseEntity<Void> priceResponse=priceProxy.updatePrice(ticker,transaction.getPrice());
        if(!handleResponse(priceResponse))return false;
        ResponseEntity<Void> orderResponse=updateOrder(transaction.getBuy_order_guid(),transaction.getQuantity());
        if(handleResponse(orderResponse))return false;
        ResponseEntity<Void> orderResponse2=updateOrder(transaction.getSell_order_guid(),transaction.getQuantity());
        return handleResponse(orderResponse2);
    }

    private ResponseEntity<Void> updateStock(Transaction transaction, String user, String ticker) {
        Position position = null;
        position.setTicker(ticker);
        position.setQuantity(-transaction.getQuantity());
        position.setUnitValue(transaction.getPrice());
        return walletProxy.addPosition(user,position);    }

    private boolean handleResponse (ResponseEntity<Void> response){
        return response.getStatusCode().isError()?  false:   true;
    }

    private ResponseEntity<Void> updateOrder(String orderID,int quantity) {
        return orderProxy.updateOrder(orderID,quantity);
    }
}
