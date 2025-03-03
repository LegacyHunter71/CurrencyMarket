package com.currencyMarket.CurrencyMarket.model.dto.api;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CurrencyRate {

    private String no;
    private LocalDate effectiveDate;
    private double bid;
    private double ask;
}
