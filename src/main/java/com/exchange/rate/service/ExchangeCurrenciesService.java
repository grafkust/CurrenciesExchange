package com.exchange.rate.service;

import com.exchange.rate.dto.CurrenciesDto;
import com.exchange.rate.dto.ExchangeCurrenciesDto;
import com.exchange.rate.model.ExchangeRate;
import com.exchange.rate.util.customException.ExchangeNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExchangeCurrenciesService {

    private final CurrenciesService currenciesService;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeCurrenciesService(CurrenciesService currenciesService, ExchangeRateService exchangeRateService) {
        this.currenciesService = currenciesService;
        this.exchangeRateService = exchangeRateService;
    }

    public ExchangeCurrenciesDto exchange(String baseCode, String targetCode, Double amount) {

        CurrenciesDto baseCurrencies = currenciesService.convertToCurrenciesDTO(currenciesService.findByCode(baseCode).get());
        CurrenciesDto targetCurrencies = currenciesService.convertToCurrenciesDTO(currenciesService.findByCode(targetCode).get());

        Optional<ExchangeRate> byBaseTargetCodes = exchangeRateService.findByBaseTargetCodes(baseCode, targetCode);
        if (byBaseTargetCodes.isPresent()) {
            Double rateAB = byBaseTargetCodes.get().getRate();
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDto(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        Optional<ExchangeRate> byTargetBaseCodes = exchangeRateService.findByBaseTargetCodes(targetCode, baseCode);
        if (byTargetBaseCodes.isPresent()) {
            Double rateAB = 1 / byTargetBaseCodes.get().getRate();
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDto(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        Optional<ExchangeRate> byUSDBaseCodes = exchangeRateService.findByBaseTargetCodes("USD", baseCode);
        Optional<ExchangeRate> byUSDTargetCodes = exchangeRateService.findByBaseTargetCodes("USD", targetCode);
        if (byUSDBaseCodes.isPresent() && byUSDTargetCodes.isPresent() ) {
            Double rateA_USD = 1 / byUSDBaseCodes.get().getRate();
            Double rateUSD_B =byUSDTargetCodes.get().getRate();
            Double rateAB = rateA_USD * rateUSD_B;

            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDto(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        Optional<ExchangeRate> byBaseUSDCodes = exchangeRateService.findByBaseTargetCodes(baseCode, "USD");
        Optional<ExchangeRate> byTargetUSDCodes = exchangeRateService.findByBaseTargetCodes(targetCode, "USD");
        if (byBaseUSDCodes.isPresent() && byTargetUSDCodes.isPresent()) {
            Double rateA_USD = byBaseUSDCodes.get().getRate();
            Double rateUSD_B = 1 / byTargetUSDCodes.get().getRate();
            Double rateAB = rateA_USD * rateUSD_B;

            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDto(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        else
            throw new ExchangeNotPossibleException();
    }
}
