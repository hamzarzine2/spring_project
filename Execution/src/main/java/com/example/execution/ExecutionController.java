package com.example.execution;

import com.example.execution.model.Position;
import com.example.execution.model.Transaction;
import com.example.execution.proxy.OrderProxy;
import com.example.execution.proxy.PriceProxy;
import com.example.execution.proxy.WalletProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ExecutionController {

    @Autowired
    ExecutionService executionService;
    @PostMapping("/execute/{ticker}/{seller}/{buyer}")
    public ResponseEntity<Void> Execute(@PathVariable String ticker, @PathVariable String seller, @PathVariable String buyer, @RequestBody Transaction transaction) {
        System.out.println("testestest");

        boolean result = executionService.executeOrder(ticker, seller, buyer, transaction);
        return result? new  ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}
