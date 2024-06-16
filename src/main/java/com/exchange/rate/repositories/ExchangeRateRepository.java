package com.exchange.rate.repositories;

import com.exchange.rate.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    List<ExchangeRate> findByBaseCurrencyId_Code(String code);

    ExchangeRate findExchangeRateByBaseCurrencyId_CodeAndTargetCurrencyId_Code(String baseCode, String targetCode);

    void deleteByBaseCurrencyId_IdAndTargetCurrencyId_Id(int bas, int target);




}
