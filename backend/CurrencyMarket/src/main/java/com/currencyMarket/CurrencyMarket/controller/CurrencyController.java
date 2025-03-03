package com.currencyMarket.CurrencyMarket.controller;

import com.currencyMarket.CurrencyMarket.model.dto.api.CurrencyRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import com.currencyMarket.CurrencyMarket.utils.NbpManagerImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/v1/currency")
public class CurrencyController {


    private final NbpManagerImpl manager;

    public CurrencyController(NbpManagerImpl manager) {
        this.manager = manager;
    }

    @GetMapping()
    public ResponseEntity<List<Rate>> getRatesInfo() {
        List<Rate> result = manager.getRatesFromApi();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{code}")
    public ResponseEntity<CurrencyRate> getCurrencyRatesInfo(@PathVariable String code) {
        CurrencyRate result = manager.getCurrencyRateFromApi(code);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
