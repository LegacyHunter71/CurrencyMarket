package com.currencyMarket.CurrencyMarket.service;

import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionRateDto;
import com.currencyMarket.CurrencyMarket.model.dto.api.NbpCurrencyRate;
import com.currencyMarket.CurrencyMarket.model.entity.CurrencyTransaction;
import com.currencyMarket.CurrencyMarket.model.enums.Status;
import com.currencyMarket.CurrencyMarket.model.mapper.CurrencyMapper;
import com.currencyMarket.CurrencyMarket.model.mapper.CurrencyTransactionMapper;
import com.currencyMarket.CurrencyMarket.repository.CurrencyTransactionRepository;
import com.currencyMarket.CurrencyMarket.utils.NbpManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionServiceImpl implements CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final NbpManagerImpl nbpManagerImpl;
    private final CurrencyMapper currencyMapper;
    private final CurrencyTransactionMapper currencyTransactionMapper;


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
    public CurrencyTransactionOfferDto addCurrencyTransaction(CurrencyTransactionInDto dto) {
        CurrencyTransaction currencyTransaction = createCurrencyTransaction(dto);
        CurrencyTransaction savedCurrencyTransaction = currencyTransactionRepository.save(currencyTransaction);

        return currencyTransactionMapper.toCurrencyTransactionOfferDto(savedCurrencyTransaction);
    }

    private CurrencyTransaction createCurrencyTransaction(CurrencyTransactionInDto dto){
        return CurrencyTransaction.builder()
                .currencyRate(dto.getCurrencyRate())
                .currencyName(dto.getCurrencyName())
                .createdDate(LocalDateTime.now())
                .quantity(dto.getQuantity())
                .currencyCode(dto.getCurrencyCode())
                .amount(getAmount(dto.getQuantity(),dto.getCurrencyRate()))
                .actionType(dto.getActionType())
                .status(Status.STARTED)
                .build();
    }

    private BigDecimal getAmount(double quantity, double currencyRate){
        BigDecimal quantityBigDecimal = BigDecimal.valueOf(quantity);
        BigDecimal currencyRateBigDecimal = BigDecimal.valueOf(currencyRate);

        return quantityBigDecimal.multiply(currencyRateBigDecimal).setScale(2, RoundingMode.HALF_UP);
    }



    @Override
    public void changeTransactionStatus(UUID uuid) {

        CurrencyTransaction transaction = currencyTransactionRepository.findById(uuid).get();
        transaction.setStatus(Status.COMPLETED);
        currencyTransactionRepository.save(transaction);
    }
}
