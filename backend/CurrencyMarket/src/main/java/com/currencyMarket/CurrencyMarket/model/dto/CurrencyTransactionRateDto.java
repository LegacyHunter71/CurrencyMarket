package com.currencyMarket.CurrencyMarket.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CurrencyTransactionRateDto {

    private String currencyName;
    private String currencyCode;
    private double bid;
    private double ask;


}
