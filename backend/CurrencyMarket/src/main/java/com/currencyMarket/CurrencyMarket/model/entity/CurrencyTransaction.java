package com.currencyMarket.CurrencyMarket.model.entity;


import com.currencyMarket.CurrencyMarket.model.enums.ActionType;
import com.currencyMarket.CurrencyMarket.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "currency_transactions")
public class CurrencyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String currencyCode;

    private String currencyName;

    private double currencyRate;

    private double quantity;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdDate;
}
