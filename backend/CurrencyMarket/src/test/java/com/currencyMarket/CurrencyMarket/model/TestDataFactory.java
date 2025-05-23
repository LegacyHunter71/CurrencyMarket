package com.currencyMarket.CurrencyMarket.model;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import com.currencyMarket.CurrencyMarket.model.entity.CurrencyTransaction;
import com.currencyMarket.CurrencyMarket.model.enums.ActionType;
import com.currencyMarket.CurrencyMarket.model.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TestDataFactory {

    public static List<Rate> createRateList() {
        return Arrays.asList(
                Rate.builder()
                        .currency("Dolar amerykański")
                        .code("USD")
                        .mid(3.23)
                        .build(),
                Rate.builder()
                        .currency("Euro")
                        .code("EUR")
                        .mid(3.44)
                        .build(),
                Rate.builder()
                        .currency("Funt szterling")
                        .code("GBP")
                        .mid(5.00)
                        .build()
        );
    }

    public static CurrencyTransactionInDto createTransactionInDto() {
        return CurrencyTransactionInDto.builder()
                .currencyCode("USD")
                .currencyName("Dolar amerykański")
                .quantity(100.5)
                .currencyRate(4.1234)
                .actionType(ActionType.BUY)
                .build();
    }
    
    public static CurrencyTransactionOfferDto createTransactionOfferDto(){
        return CurrencyTransactionOfferDto.builder()
                .currencyCode("USD")
                .currencyName("Dolar amerykański")
                .quantity(100.5)
                .currencyRate(4.1234)
                .actionType(ActionType.BUY)
                .build();
    }


    public static CurrencyTransaction createCurrencyTransaction() {
        return CurrencyTransaction.builder()
                .currencyCode("USD")
                .currencyName("Dolar amerykański")
                .quantity(100.5)
                .currencyRate(4.1234)
                .actionType(ActionType.BUY)
                .status(Status.STARTED)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
