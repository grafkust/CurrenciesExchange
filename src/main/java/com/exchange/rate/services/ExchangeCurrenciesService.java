package com.exchange.rate.services;

import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.dto.ExchangeCurrenciesDTO;
import com.exchange.rate.util.customExceptions.ExchangeNotPossibleException;
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

    public ExchangeCurrenciesDTO exchange(String baseCode, String targetCode, Double amount) {

        CurrenciesDTO baseCurrencies = currenciesService.findDTOByCode(baseCode);
        CurrenciesDTO targetCurrencies = currenciesService.findDTOByCode(targetCode);

        if (exchangeRateService.checkCodePairExist(baseCode, targetCode)) {
            Double rateAB = exchangeRateService.findByBaseTargetCodes(baseCode, targetCode).getRate();
            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        if (exchangeRateService.checkCodePairExist(targetCode, baseCode)) {
            Double rateAB = 1 / exchangeRateService.findByBaseTargetCodes(targetCode, baseCode).getRate();

            Double convertToAmountAB = amount * rateAB;
            return new ExchangeCurrenciesDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        if (exchangeRateService.checkCodePairExist("USD",baseCode) && exchangeRateService.checkCodePairExist("USD", targetCode) ) {
            Double rateA_USD = 1 / exchangeRateService.findByBaseTargetCodes("USD",baseCode).getRate();
            Double rateUSD_B = exchangeRateService.findByBaseTargetCodes("USD",targetCode).getRate();
            Double rateAB = rateA_USD * rateUSD_B;

            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        if (exchangeRateService.checkCodePairExist(baseCode,"USD") && exchangeRateService.checkCodePairExist(targetCode, "USD")) {
            Double rateA_USD = exchangeRateService.findByBaseTargetCodes(baseCode,"USD").getRate();
            Double rateUSD_B = 1 / exchangeRateService.findByBaseTargetCodes(targetCode, "USD").getRate();
            Double rateAB = rateA_USD * rateUSD_B;

            Double convertToAmountAB = amount * rateAB;

            return new ExchangeCurrenciesDTO(baseCurrencies, targetCurrencies,rateAB, amount, convertToAmountAB);
        }

        else
            throw new ExchangeNotPossibleException();
    }
}
