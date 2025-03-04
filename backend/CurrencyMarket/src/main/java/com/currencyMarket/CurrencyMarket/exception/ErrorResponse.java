package com.currencyMarket.CurrencyMarket.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ErrorResponse {

    private final UUID uuid;
    private final String message;

    public ErrorResponse(UUID uuid, String message) {
        this.uuid = uuid;
        this.message = message;
    }

}
