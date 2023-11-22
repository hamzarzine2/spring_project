package com.example.wallet;

import com.example.wallet.repositories.InvestorProxy;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {

    private final WalletService service;
    private final InvestorProxy investorproxy;

    public WalletController(WalletService walletService, InvestorProxy investorProxy) {
        this.service = walletService;
        this.investorproxy = investorProxy;
    }

    //todo vérifier cette méthode
    @GetMapping("/wallet/{username}/net-worth")
    public ResponseEntity<Double> getNetWorth(@PathVariable String username) {
        //vérifier si l'user existe sinon 404
        if(investorproxy.getAllInvestors().stream().noneMatch(investor -> investor.getUsername().equals(username)))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        double netWorth = service.getNetWorth(username);
        return new ResponseEntity<>(netWorth, HttpStatus.OK);
    }

    @GetMapping("/wallet/{username}")
    public ResponseEntity<List<Position>> getOpenedPositions(@PathVariable String username) {
        //vérifier si l'user existe sinon 404
        if(investorproxy.getAllInvestors().stream().noneMatch(investor -> investor.getUsername().equals(username)))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        List<Position> positions = service.getOpenedPositions(username);
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }
}
