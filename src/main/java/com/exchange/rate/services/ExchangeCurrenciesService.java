package com.exchange.rate.services;

import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.dto.ExchangeDTO;
import com.exchange.rate.util.customExceptions.CodePairNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public ExchangeDTO exchange(String baseCode, String targetCode, Double amount) {

        CurrenciesDTO baseCurrencies = currenciesService.findByCode(baseCode);
        CurrenciesDTO targetCurrencies = currenciesService.findByCode(targetCode);

        if (exchangeRateService.checkCodePairExist(baseCode, targetCode)){
            Double rateAB = exchangeRateService.findByCodePair(baseCode, targetCode).getRate();
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        if (exchangeRateService.checkCodePairExist(targetCode, baseCode)) {
            Double rateAB = 1 / exchangeRateService.findByCodePair(targetCode, baseCode).getRate();
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        if (exchangeRateService.checkCodePairExist("USD",baseCode) && exchangeRateService.checkCodePairExist("USD", targetCode) ) {
            Double rateA_USD = 1 / exchangeRateService.findByCodePair("USD",baseCode).getRate();
            Double rateUSD_B = exchangeRateService.findByCodePair("USD",targetCode).getRate();
            Double rateAB = rateA_USD * rateUSD_B;
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        if (exchangeRateService.checkCodePairExist(baseCode,"USD") && exchangeRateService.checkCodePairExist(targetCode, "USD")) {
            Double rateA_USD = exchangeRateService.findByCodePair(baseCode,"USD").getRate();
            Double rateUSD_B = 1 / exchangeRateService.findByCodePair(targetCode, "USD").getRate();
            Double rateAB = rateA_USD * rateUSD_B;
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        else
            throw new CodePairNotFoundException();
    }
}
