package com.currencyMarket.CurrencyMarket.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CurrencyDto {

    String currencyCode;
    String currencyName;
}
