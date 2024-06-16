package com.exchange.rate.services;

import com.exchange.rate.dto.ExchangeRateDTO;
import com.exchange.rate.dto.RateDTO;
import com.exchange.rate.models.Currencies;
import com.exchange.rate.models.ExchangeRate;
import com.exchange.rate.repositories.ExchangeRateRepository;
import com.exchange.rate.util.UtilsToServices;
import com.exchange.rate.util.customExceptions.CodePairIsAlreadyExistException;
import com.exchange.rate.util.customExceptions.CodePairNotFoundException;
import com.exchange.rate.util.customExceptions.ExchangeRateWithCurrentBaseCodeNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final UtilsToServices utilsToServices;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, UtilsToServices utilsToServices) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.utilsToServices = utilsToServices;
    }

    public List<ExchangeRateDTO> findAll() {
        return exchangeRateRepository.findAll().stream().map(this :: convertToExchangeRateDTO).collect(Collectors.toList());
    }

    public List<ExchangeRateDTO> findByBaseCurrenciesCode (String code) {
        List<ExchangeRate> baseCurrency = exchangeRateRepository.findByBaseCurrencyId_Code(code);
        if (baseCurrency.isEmpty())
            throw new ExchangeRateWithCurrentBaseCodeNotExist();
        return baseCurrency.stream().map(this:: convertToExchangeRateDTO).collect(Collectors.toList());
    }

    public ExchangeRateDTO findByCodePair(String codePair){
        String baseCode = utilsToServices.splitCodePair(codePair, 0);
        String targetCode = utilsToServices.splitCodePair(codePair, 1);

        if (!checkCodePairExist(baseCode,targetCode))
            throw new CodePairNotFoundException();

        ExchangeRate exchangeRateByCodePair = findByCodePair(baseCode,targetCode);

        return utilsToServices.convertToExchangeRateDTO(exchangeRateByCodePair);
    }

    public ExchangeRate findByCodePair(String baseCode, String targetCode){
        return exchangeRateRepository.findExchangeRateByBaseCurrencyId_CodeAndTargetCurrencyId_Code(baseCode, targetCode);
    }

    public boolean checkCodePairExist (String baseCode, String targetCode) {
        return exchangeRateRepository.findExchangeRateByBaseCurrencyId_CodeAndTargetCurrencyId_Code(baseCode, targetCode) != null;
    }


    @Transactional
    public void update(String codePair, RateDTO newRate){
        ExchangeRateDTO rateToUpdate = findByCodePair(codePair);
        rateToUpdate.setRate(newRate.getRate());
    }


    @Transactional
    public void delete(String codePair) {
        ExchangeRate byCodePair = utilsToServices.convertToExchangeRate(findByCodePair(codePair));
        int baseID = byCodePair.getBaseCurrencyId().getId();
        int targetID = byCodePair.getTargetCurrencyId().getId();

        exchangeRateRepository.deleteByBaseCurrencyId_IdAndTargetCurrencyId_Id(baseID,targetID);
    }

    @Transactional
    public void save(ExchangeRateDTO exchangeRateDTO) {
        String baseCode = exchangeRateDTO.getBaseCurrencyDTO().getCode();
        String targetCode = exchangeRateDTO.getTargetCurrencyDTO().getCode();

        if (checkCodePairExist(baseCode, targetCode) || checkCodePairExist(targetCode, baseCode))
            throw new CodePairIsAlreadyExistException();

        exchangeRateRepository.save(enrich(exchangeRateDTO));
    }

    private ExchangeRate enrich(ExchangeRateDTO exchangeRateDTO){

        Currencies baseCurrencies = utilsToServices.convertToCurrencies(exchangeRateDTO.getBaseCurrencyDTO());
        Currencies targetCurrencies = utilsToServices.convertToCurrencies(exchangeRateDTO.getTargetCurrencyDTO());
        Double rate = exchangeRateDTO.getRate();
        return new ExchangeRate(baseCurrencies,targetCurrencies,rate);
    }

    private ExchangeRateDTO convertToExchangeRateDTO(ExchangeRate exchangeRate) {
        return utilsToServices.convertToExchangeRateDTO(exchangeRate);
    }





}
