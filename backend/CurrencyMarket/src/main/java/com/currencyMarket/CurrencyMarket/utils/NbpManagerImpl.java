package com.currencyMarket.CurrencyMarket.utils;

import com.currencyMarket.CurrencyMarket.model.dto.api.NbpCurrencyRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.NbpRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NbpManagerImpl implements NbpManager {

    private final static String NBP_RATE_URL = "https://api.nbp.pl/api/exchangerates/tables/a";
    private final static String NBP_CURRENCY_RATE_URL = "https://api.nbp.pl/api/exchangerates/rates/c/";

    private final WebClient.Builder webClientBuilder;

    @Override
    public List<Rate> getRatesFromApi() {
        WebClient webClient = webClientBuilder.baseUrl(NBP_RATE_URL).build();

        NbpRate[] nbpRates = webClient.get()
                .retrieve()
                .bodyToMono(NbpRate[].class)
                .block();

//        if (nbpRates != null && nbpRates.length > 0) {
//            return nbpRates[0].getRates();
//        } else {
//            return List.of();
//        }

        return nbpRates[0].getRates();
    }

    @Override
    public NbpCurrencyRate getCurrencyRateFromApi(String code) {
        WebClient webClient = webClientBuilder.baseUrl(createUrl(code)).build();

        NbpCurrencyRate nbpCurrencyRate = webClient.get()
                .retrieve()
                .bodyToMono(NbpCurrencyRate.class)
                .block();

        if (nbpCurrencyRate != null && !nbpCurrencyRate.getRates().isEmpty()) {
            return nbpCurrencyRate;
        } else {
            return null;
        }
    }

    private String createUrl(String code) {
        return NBP_CURRENCY_RATE_URL + code;
    }
}
