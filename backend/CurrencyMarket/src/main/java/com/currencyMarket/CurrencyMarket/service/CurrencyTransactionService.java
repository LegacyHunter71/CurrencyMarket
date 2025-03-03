package com.currencyMarket.CurrencyMarket.service;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionRateDto;

import java.util.List;
import java.util.UUID;

public interface CurrencyTransactionService {

    List<CurrencyDto> getCurrency();

    CurrencyTransactionRateDto getCurrencyTransactionRate(String code);

    CurrencyTransactionOfferDto addCurrencyTransaction(CurrencyTransactionInDto dto);

    void changeTransactionStatus(UUID uuid);
}
