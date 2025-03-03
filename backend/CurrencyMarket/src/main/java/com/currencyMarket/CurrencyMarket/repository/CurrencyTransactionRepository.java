package com.currencyMarket.CurrencyMarket.repository;

import com.currencyMarket.CurrencyMarket.model.entity.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyTransactionRepository extends JpaRepository<CurrencyTransaction, UUID> {
}
