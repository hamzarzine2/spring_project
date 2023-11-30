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

@Service
public class ExecutionService {

    @Autowired
    private WalletProxy walletProxy;

    @Autowired
    private PriceProxy priceProxy;

    @Autowired
    private OrderProxy orderProxy;

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

    private Position createPositionCash(double amount){
        Position positionCash = new Position();
        positionCash.setTicker("CASH");
        positionCash.setQuantity(amount);
        positionCash.setUnitValue(1);
        return positionCash;
    }

    private boolean handleResponse(ResponseEntity<Void> response) {
        System.out.println(!response.getStatusCode().isError());
        return !response.getStatusCode().isError();
    }

    private boolean updateOrder(String orderId, int quantity) {
        Order orderTemp = new Order();
        orderTemp.setFilled(quantity);
        return !orderProxy.updateOrder(orderId, orderTemp).getStatusCode().isError();
    }
}
