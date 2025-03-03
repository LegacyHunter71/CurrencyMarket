package com.currencyMarket.CurrencyMarket.model.mapper;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.entity.CurrencyTransaction;
import org.springframework.stereotype.Component;

@Component
public class CurrencyTransactionMapper {

    public CurrencyTransactionOfferDto toCurrencyTransactionOfferDto(CurrencyTransaction currencyTransaction){
    return CurrencyTransactionOfferDto.builder()
            .currencyRate(currencyTransaction.getCurrencyRate())
            .quantity(currencyTransaction.getQuantity())
            .currencyName(currencyTransaction.getCurrencyName())
            .currencyCode(currencyTransaction.getCurrencyCode())
            .actionType(currencyTransaction.getActionType())
            .amount(currencyTransaction.getAmount())
            .id(currencyTransaction.getId())
            .build();
    }
}
