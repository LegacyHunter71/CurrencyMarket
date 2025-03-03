package com.currencyMarket.CurrencyMarket.utils;

import com.currencyMarket.CurrencyMarket.model.dto.api.NbpCurrencyRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;

import java.util.List;

public interface NbpManager {

    List<Rate> getRatesFromApi();

    NbpCurrencyRate getCurrencyRateFromApi(String code);
}
