package com.currencyMarket.CurrencyMarket.controller;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionRateDto;
import com.currencyMarket.CurrencyMarket.service.CurrencyTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currency")
public class CurrencyController {


    private final CurrencyTransactionServiceImpl service;

    @GetMapping()
    public ResponseEntity<List<CurrencyDto>> getRatesInfo() {
        List<CurrencyDto> result = service.getCurrency();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{code}")
    public ResponseEntity<CurrencyTransactionRateDto> getCurrencyRatesInfo(@PathVariable String code) {
        CurrencyTransactionRateDto result = service.getCurrencyTransactionRate(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CurrencyTransactionOfferDto> addCurrencyTransaction(@RequestBody CurrencyTransactionInDto dto) {
        CurrencyTransactionOfferDto result = service.addCurrencyTransaction(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{uuid}/complete")
    public ResponseEntity<Void> completeTransaction(@PathVariable UUID uuid) {
        service.changeTransactionStatus(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
