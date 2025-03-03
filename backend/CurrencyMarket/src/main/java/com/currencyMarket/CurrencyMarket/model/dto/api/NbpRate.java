package com.currencyMarket.CurrencyMarket.model.dto.api;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NbpRate {
    private String table;
    private String no;
    private LocalDate effectiveDate;
    private List<Rate> rates;
}
