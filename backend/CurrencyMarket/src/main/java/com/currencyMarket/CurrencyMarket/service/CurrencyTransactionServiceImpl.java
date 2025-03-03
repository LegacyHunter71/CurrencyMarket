package com.currencyMarket.CurrencyMarket.service;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionRateDto;
import com.currencyMarket.CurrencyMarket.model.dto.api.NbpCurrencyRate;
import com.currencyMarket.CurrencyMarket.model.mapper.CurrencyMapper;
import com.currencyMarket.CurrencyMarket.repository.CurrencyTransactionRepository;
import com.currencyMarket.CurrencyMarket.utils.NbpManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionServiceImpl implements CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final NbpManagerImpl nbpManagerImpl;
    private final CurrencyMapper currencyMapper;


    @Override
    public List<CurrencyDto> getCurrency() {
        List<CurrencyDto> result = currencyMapper.toCurrenciesDtoList(nbpManagerImpl.getRatesFromApi());
        return result;
    }

    @Override
    public CurrencyTransactionRateDto getCurrencyTransactionRate(String code) {
        NbpCurrencyRate currencyRate = nbpManagerImpl.getCurrencyRateFromApi(code);
        return createCurrencyTransactionRateDto(currencyRate);
    }

    private CurrencyTransactionRateDto createCurrencyTransactionRateDto(NbpCurrencyRate rate) {
        return CurrencyTransactionRateDto.builder()
                .currencyCode(rate.getCode())
                .currencyName(rate.getCurrency())
                .ask(rate.getRates().getFirst().getAsk())
                .bid(rate.getRates().getFirst().getBid())
                .build();
    }

    @Override
    public CurrencyTransactionOfferDto createCurrencyTransactionOffer(CurrencyTransactionInDto dto) {
        return null;
    }

    @Override
    public void changeTransactionStatus(UUID uuid) {

    }
}
