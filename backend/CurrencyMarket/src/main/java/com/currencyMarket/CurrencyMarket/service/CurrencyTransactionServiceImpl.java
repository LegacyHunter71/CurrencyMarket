package com.currencyMarket.CurrencyMarket.service;

import com.currencyMarket.CurrencyMarket.exception.BusinessException;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionRateDto;
import com.currencyMarket.CurrencyMarket.model.dto.api.NbpCurrencyRate;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import com.currencyMarket.CurrencyMarket.model.entity.CurrencyTransaction;
import com.currencyMarket.CurrencyMarket.model.enums.Status;
import com.currencyMarket.CurrencyMarket.model.mapper.CurrencyTransactionMapper;
import com.currencyMarket.CurrencyMarket.repository.CurrencyTransactionRepository;
import com.currencyMarket.CurrencyMarket.utils.NbpManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionServiceImpl implements CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final NbpManagerImpl nbpManagerImpl;
    private final CurrencyTransactionMapper currencyTransactionMapper;
    private final Environment environment;


    @Override
    public List<CurrencyDto> getCurrency() {
        List<CurrencyDto> allCurrenciesDtoList =createCurrenciesDtoList(nbpManagerImpl.getRatesFromApi());
        List<String> currencyCodes = getCurrencyCodesFromProperties();
        return filterCurrencies(currencyCodes, allCurrenciesDtoList);
    }

    private List<String> getCurrencyCodesFromProperties() {
        String currencyCodesString = environment.getProperty("currency.codes");
        if (currencyCodesString != null) {
            return Arrays.asList(currencyCodesString.split(","));
        } else {
            return List.of();
        }
    }

    private List<CurrencyDto> filterCurrencies(List<String> currencyCodes,List<CurrencyDto> currencies) {
        return currencies.stream()
                .filter(dto -> currencyCodes.contains(dto.getCurrencyCode()))
                .collect(Collectors.toList());
    }

    private CurrencyDto createCurrencyDto(Rate rate) {
        return CurrencyDto.builder()
                .currencyCode(rate.getCode())
                .currencyName(rate.getCurrency())
                .build();
    }

    private List<CurrencyDto> createCurrenciesDtoList(List<Rate> rates) {
        return rates.stream()
                .map(this::createCurrencyDto)
                .collect(Collectors.toList());
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
    public CurrencyTransactionOfferDto addCurrencyTransaction(CurrencyTransactionInDto dto) {
        CurrencyTransaction currencyTransaction = createCurrencyTransaction(dto);
        CurrencyTransaction savedCurrencyTransaction = currencyTransactionRepository.save(currencyTransaction);

        return currencyTransactionMapper.toCurrencyTransactionOfferDto(savedCurrencyTransaction);
    }

    private CurrencyTransaction createCurrencyTransaction(CurrencyTransactionInDto dto) {
        return CurrencyTransaction.builder()
                .currencyRate(dto.getCurrencyRate())
                .currencyName(dto.getCurrencyName())
                .createdDate(LocalDateTime.now())
                .quantity(dto.getQuantity())
                .currencyCode(dto.getCurrencyCode())
                .amount(getAmount(dto.getQuantity(), dto.getCurrencyRate()))
                .actionType(dto.getActionType())
                .status(Status.STARTED)
                .build();
    }

    private BigDecimal getAmount(double quantity, double currencyRate) {
        BigDecimal quantityBigDecimal = BigDecimal.valueOf(quantity);
        BigDecimal currencyRateBigDecimal = BigDecimal.valueOf(currencyRate);

        return quantityBigDecimal.multiply(currencyRateBigDecimal).setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public void changeTransactionStatus(UUID uuid) {

        Optional<CurrencyTransaction> transaction = currencyTransactionRepository.findById(uuid);
        if(transaction.isEmpty()){
            throw new BusinessException("Unknown transaction", UUID.fromString("caf5cea3-aadc-4453-900d-e40f34480066"));
        }
        CurrencyTransaction currencyTransaction = transaction.get();
        currencyTransaction.setStatus(Status.COMPLETED);
        currencyTransactionRepository.save(currencyTransaction);
    }
}
