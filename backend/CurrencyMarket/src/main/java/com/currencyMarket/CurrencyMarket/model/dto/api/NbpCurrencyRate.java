package com.currencyMarket.CurrencyMarket.model.dto.api;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NbpCurrencyRate {

    private String table;
    private String currency;
    private String code;
    private List<CurrencyRate> rates;
}
