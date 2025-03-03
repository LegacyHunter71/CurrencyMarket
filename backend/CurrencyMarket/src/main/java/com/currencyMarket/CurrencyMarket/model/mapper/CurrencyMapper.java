package com.currencyMarket.CurrencyMarket.model.mapper;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyMapper {

    private CurrencyDto toCurrencyDto(Rate rate) {
        return CurrencyDto.builder()
                .currencyCode(rate.getCode())
                .currencyName(rate.getCurrency())
                .build();
    }

    public List<CurrencyDto> toCurrenciesDtoList(List<Rate> rates) {
        return rates.stream()
                .map(this::toCurrencyDto)
                .collect(Collectors.toList());
    }
}
