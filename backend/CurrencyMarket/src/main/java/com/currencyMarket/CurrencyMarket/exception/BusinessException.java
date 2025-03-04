package com.currencyMarket.CurrencyMarket.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BusinessException extends RuntimeException {

    private final UUID uuid;

    public BusinessException(String message, UUID uuid) {
        super(message);
        this.uuid = uuid;
    }

}
