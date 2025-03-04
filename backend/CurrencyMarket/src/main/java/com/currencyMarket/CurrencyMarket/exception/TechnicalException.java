package com.currencyMarket.CurrencyMarket.exception;


import lombok.Getter;

import java.util.UUID;

@Getter
public class TechnicalException extends RuntimeException {

    private final UUID uuid;

    public TechnicalException(String message, UUID uuid) {
        super(message);
        this.uuid = uuid;
    }

}
