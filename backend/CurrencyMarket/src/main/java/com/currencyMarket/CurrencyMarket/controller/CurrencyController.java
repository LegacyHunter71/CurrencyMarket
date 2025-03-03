package com.currencyMarket.CurrencyMarket.controller;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionRateDto;
import com.currencyMarket.CurrencyMarket.service.CurrencyTransactionServiceImpl;
import com.currencyMarket.CurrencyMarket.utils.NbpManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currency")
public class CurrencyController {


    private final NbpManagerImpl manager;
    private final CurrencyTransactionServiceImpl service;

    @GetMapping()
    public ResponseEntity<List<CurrencyDto>> getRatesInfo() {
        List<CurrencyDto> result = service.getCurrency();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{code}")
    public ResponseEntity<CurrencyTransactionRateDto> getCurrencyRatesInfo(@PathVariable String code) {
        CurrencyTransactionRateDto  result = service.getCurrencyTransactionRate(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
