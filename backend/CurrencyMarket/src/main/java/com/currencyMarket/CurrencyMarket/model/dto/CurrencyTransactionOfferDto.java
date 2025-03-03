package com.currencyMarket.CurrencyMarket.model.dto;

import com.currencyMarket.CurrencyMarket.model.enums.ActionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CurrencyTransactionOfferDto {

    private String currencyName;
    private String currencyCode;
    private double currencyRate;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private double quantity;
    private BigDecimal amount;
}
