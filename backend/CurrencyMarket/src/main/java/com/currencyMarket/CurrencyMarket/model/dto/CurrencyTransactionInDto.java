package com.currencyMarket.CurrencyMarket.model.dto;

import com.currencyMarket.CurrencyMarket.model.enums.ActionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CurrencyTransactionInDto {

    private String currencyCode;
    private String currencyName;
    private double quantity;
    private double currencyRate;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
}
