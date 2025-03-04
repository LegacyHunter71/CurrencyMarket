package com.currencyMarket.CurrencyMarket.utils;

import com.currencyMarket.CurrencyMarket.exception.TechnicalException;
import com.currencyMarket.CurrencyMarket.model.dto.api.NbpCurrencyRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.NbpRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NbpManagerImpl implements NbpManager {

    private final static String NBP_RATE_URL = "https://api.nbp.pl/api/exchangerates/tables/a";
    private final static String NBP_CURRENCY_RATE_URL = "https://api.nbp.pl/api/exchangerates/rates/c/";

    private final WebClient.Builder webClientBuilder;

    @Override
    public List<Rate> getRatesFromApi() {
        WebClient webClient = webClientBuilder.baseUrl(NBP_RATE_URL).build();

        try {
            NbpRate[] nbpRates = webClient.get()
                    .retrieve()
                    .bodyToMono(NbpRate[].class)
                    .block();

            if (nbpRates != null && nbpRates.length > 0) {
                return nbpRates[0].getRates();
            } else {
                throw new TechnicalException("API provider returned empty data", UUID.fromString("a7c6a465-504a-47d9-bb52-e0bf6ccf9f4a"));
            }
        } catch (WebClientResponseException ex) {
            throw new TechnicalException("API provider returned error status: " + ex.getStatusCode(), UUID.fromString("a7c6a465-504a-47d9-bb52-e0bf6ccf9f4b"));
        } catch (WebClientException ex) {
            throw new TechnicalException("Failed to connect to API provider: " + ex.getMessage(), UUID.fromString("a7c6a465-504a-47d9-bb52-e0bf6ccf9f4c"));
        }
    }

    @Override
    public NbpCurrencyRate getCurrencyRateFromApi(String code) {
        WebClient webClient = webClientBuilder.baseUrl(createUrl(code)).build();

        try {
            NbpCurrencyRate nbpCurrencyRate = webClient.get()
                    .retrieve()
                    .bodyToMono(NbpCurrencyRate.class)
                    .block();

            if (nbpCurrencyRate != null && !nbpCurrencyRate.getRates().isEmpty()) {
                return nbpCurrencyRate;
            } else {
                throw new TechnicalException("API provider returned empty data", UUID.fromString("ff4f31be-a764-4e04-b815-6e00567f255a"));
            }
        } catch (WebClientResponseException ex) {
            throw new TechnicalException("API provider returned error status: " + ex.getStatusCode(), UUID.fromString("ff4f31be-a764-4e04-b815-6e00567f255b"));
        } catch (WebClientException ex) {
            throw new TechnicalException("Failed to connect to API provider: " + ex.getMessage(), UUID.fromString("ff4f31be-a764-4e04-b815-6e00567f255c"));
        }
    }

    private String createUrl(String code) {
        return NBP_CURRENCY_RATE_URL + code;
    }
}
