package com.exchange.rate.repository;

import com.exchange.rate.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    List<ExchangeRate> findByBaseCurrencyId_Code(String code);

   Optional <ExchangeRate> findExchangeRateByBaseCurrencyId_CodeAndTargetCurrencyId_Code(String baseCode, String targetCode);





}
