package com.currencyMarket.CurrencyMarket.model.dto.api;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Rate {

    private String currency;
    private String code;
    private double mid;
}
