package com.currencyMarket.CurrencyMarket.service;

import com.currencyMarket.CurrencyMarket.exception.BusinessException;
import com.currencyMarket.CurrencyMarket.model.TestDataFactory;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionInDto;
import com.currencyMarket.CurrencyMarket.model.dto.CurrencyTransactionOfferDto;
import com.currencyMarket.CurrencyMarket.model.dto.api.Rate;
import com.currencyMarket.CurrencyMarket.model.entity.CurrencyTransaction;
import com.currencyMarket.CurrencyMarket.model.enums.ActionType;
import com.currencyMarket.CurrencyMarket.model.enums.Status;
import com.currencyMarket.CurrencyMarket.model.mapper.CurrencyTransactionMapper;
import com.currencyMarket.CurrencyMarket.repository.CurrencyTransactionRepository;
import com.currencyMarket.CurrencyMarket.utils.NbpManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock
    private NbpManagerImpl nbpManagerImpl;

    @Mock
    private Environment environment;

    @Mock
    private CurrencyTransactionRepository currencyTransactionRepository;

    @Mock
    private CurrencyTransactionMapper currencyTransactionMapper;

    @InjectMocks
    private CurrencyTransactionServiceImpl currencyService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getCurrency_shouldFilterAndReturnCurrenciesBasedOnProperties() {
        // Given
        List<Rate> nbpRates = TestDataFactory.createRateList();

        when(nbpManagerImpl.getRatesFromApi()).thenReturn(nbpRates);
        when(environment.getProperty("currency.codes")).thenReturn("USD,GBP");

        // When
        List<CurrencyDto> result = currencyService.getCurrency();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(dto -> "USD".equals(dto.getCurrencyCode()) && "Dolar amerykański".equals(dto.getCurrencyName())));
        assertTrue(result.stream().anyMatch(dto -> "GBP".equals(dto.getCurrencyCode()) && "Funt szterling".equals(dto.getCurrencyName())));

        assertFalse(result.stream().anyMatch(dto -> "EUR".equals(dto.getCurrencyCode())));
        assertFalse(result.stream().anyMatch(dto -> "PLN".equals(dto.getCurrencyCode())));


        verify(nbpManagerImpl).getRatesFromApi();
        verify(environment).getProperty("currency.codes");
    }

    @Test
    void getCurrency_shouldReturnEmptyListWhenNoCurrencyCodesProperty() {
        // Given
        List<Rate> nbpRates = TestDataFactory.createRateList();
        when(nbpManagerImpl.getRatesFromApi()).thenReturn(nbpRates);
        when(environment.getProperty("currency.codes")).thenReturn(null);

        // When
        List<CurrencyDto> result = currencyService.getCurrency();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(nbpManagerImpl).getRatesFromApi();
        verify(environment).getProperty("currency.codes");
    }

    @Test
    void getCurrency_shouldReturnEmptyListWhenEmptyCurrencyCodesProperty() {
        // Given
        List<Rate> nbpRates = TestDataFactory.createRateList();
        when(nbpManagerImpl.getRatesFromApi()).thenReturn(nbpRates);
        when(environment.getProperty("currency.codes")).thenReturn("");

        // When
        List<CurrencyDto> result = currencyService.getCurrency();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(nbpManagerImpl).getRatesFromApi();
        verify(environment).getProperty("currency.codes");
    }

    @Test
    void addCurrencyTransaction_shouldSaveAndMapTransactionCorrectly() {
        // Given

        CurrencyTransactionInDto inDto = TestDataFactory.createTransactionInDto();
        UUID generatedId = UUID.randomUUID();
        //100.5 * 4.1234 = 414.4517 --> 414.45
        BigDecimal expectedAmount = BigDecimal.valueOf(inDto.getQuantity() * inDto.getCurrencyRate())
                .setScale(2, RoundingMode.HALF_UP);

        when(currencyTransactionRepository.save(any(CurrencyTransaction.class)))
                .thenAnswer(invocation -> {
                    CurrencyTransaction transactionToSave = invocation.getArgument(0);
                    transactionToSave.setId(generatedId);
                    transactionToSave.setCreatedDate(LocalDateTime.now());
                    return transactionToSave;
                });
        CurrencyTransactionOfferDto expectedOfferDto = TestDataFactory.createTransactionOfferDto();
        expectedOfferDto.setAmount(expectedAmount);


        when(currencyTransactionMapper.toCurrencyTransactionOfferDto(any(CurrencyTransaction.class)))
                .thenReturn(expectedOfferDto);

        // When
        CurrencyTransactionOfferDto result = currencyService.addCurrencyTransaction(inDto);

        // Then
        assertNotNull(result);
        assertEquals(expectedOfferDto, result);

        verify(currencyTransactionMapper).toCurrencyTransactionOfferDto(any(CurrencyTransaction.class));
    }
    @Test
    void changeTransactionStatus_shouldUpdateStatusToCompletedAndSave() {
        // Given
        UUID transactionId = UUID.randomUUID();
        CurrencyTransaction existingTransaction = TestDataFactory.createCurrencyTransaction();
        existingTransaction.setId(transactionId);

        when(currencyTransactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        // When
        currencyService.changeTransactionStatus(transactionId);

        // Then
        verify(currencyTransactionRepository).findById(transactionId);
        verify(currencyTransactionRepository).save(argThat(savedTransaction ->
                savedTransaction.getId().equals(transactionId) &&
                        savedTransaction.getStatus() == Status.COMPLETED
        ));
        assertEquals("USD", existingTransaction.getCurrencyCode());
        assertEquals(100.5, existingTransaction.getQuantity());
        assertEquals(ActionType.BUY, existingTransaction.getActionType());
    }

    @Test
    void changeTransactionStatus_shouldThrowBusinessException_whenTransactionNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        when(currencyTransactionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            currencyService.changeTransactionStatus(nonExistentId);
        });

        assertEquals("Unknown transaction", thrown.getMessage());
        assertEquals(UUID.fromString("caf5cea3-aadc-4453-900d-e40f34480066"), thrown.getUuid());

        verify(currencyTransactionRepository).findById(nonExistentId);
        verify(currencyTransactionRepository, never()).save(any());
    }

}