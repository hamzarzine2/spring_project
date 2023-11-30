package com.example.wallet;

import com.example.wallet.models.Position;
import com.example.wallet.models.PositionDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {

    private final WalletService service;
    public WalletController(WalletService walletService) {
        this.service = walletService;
    }

    @GetMapping("/wallet/{username}/net-worth")
    public ResponseEntity<Double> getNetWorth(@PathVariable("username") String username) {
        double netWorth = service.getNetWorth(username);
        return new ResponseEntity<>(netWorth, HttpStatus.OK);
    }

    @GetMapping("/wallet/{username}")
    public ResponseEntity<List<PositionDTO>> getOpenedPositions(@PathVariable String username) {
        List<PositionDTO> positions = service.getOpenedPositions(username);
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    @PostMapping("/wallet/{username}")
    public ResponseEntity<List<PositionDTO>> addPosition(@PathVariable String username, @RequestBody
        List<PositionDTO> positionDTOs) {
        return new ResponseEntity<>(service.addPosition(username, positionDTOs), HttpStatus.OK);
    }
}
