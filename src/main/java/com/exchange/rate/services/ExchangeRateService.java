package com.exchange.rate.services;

import com.exchange.rate.dto.*;
import com.exchange.rate.models.Currencies;
import com.exchange.rate.models.ExchangeRate;
import com.exchange.rate.repositories.ExchangeRateRepository;
import com.exchange.rate.util.customExceptions.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrenciesService currenciesService;
    private final ModelMapper modelMapper;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrenciesService currenciesService, ModelMapper modelMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currenciesService = currenciesService;
        this.modelMapper = modelMapper;
    }

    public AllExchangeRateDTO findAll() {
        List<ExchangeRate> allExchangeRate = exchangeRateRepository.findAll();

        if (allExchangeRate.isEmpty())
            throw new NoDataException();

        List<ExchangeRateDTO> allExchangeRateDTO = allExchangeRate.stream().map(this::convertToExchangeRateDTO).collect(Collectors.toList());
        return new AllExchangeRateDTO(allExchangeRateDTO);
    }

    public AllExchangeRateDTO findByBaseCode(String code) {
        List<ExchangeRate> baseCurrency = exchangeRateRepository.findByBaseCurrencyId_Code(code);

        if (baseCurrency.isEmpty())
            throw new ExchangeRateWithCurrentBaseCodeNotExist();

        List<ExchangeRateDTO> allExchangeRateDTO = baseCurrency.stream().map(this::convertToExchangeRateDTO).collect(Collectors.toList());
        return new AllExchangeRateDTO(allExchangeRateDTO);
    }

    public ExchangeRateDTO findDTOByCodePair (String codePair){

        ExchangeRate exchangeRateByCodePair = findByCodePair(codePair);

        return convertToExchangeRateDTO(exchangeRateByCodePair);
    }

    @Transactional
    public void update(RateDTO newRate){
        String codePair = newRate.getBaseCurrenciesCode()+newRate.getTargetCurrenciesCode();

        ExchangeRate exchangeRateToUpdate = findByCodePair(codePair);

        exchangeRateToUpdate.setRate(newRate.getRate());
    }

    private ExchangeRate findByCodePair (String codePair){

        String baseCode = splitCodePair(codePair, 0);
        String targetCode = splitCodePair(codePair, 1);

        if (!checkCodePairExist(baseCode,targetCode))
            throw new CodePairNotFoundException();

        return findByBaseTargetCodes(baseCode,targetCode);
    }


    @Transactional
    public void save(RateDTO rateDTO) {

        String baseCode = rateDTO.getBaseCurrenciesCode();
        String targetCode = rateDTO.getTargetCurrenciesCode();

        if (baseCode.equals(targetCode))
            throw new CurrenciesIsIdenticalException();

        if (currenciesService.checkExistByCode(baseCode) && currenciesService.checkExistByCode(targetCode)) {
            if (checkCodePairExist(baseCode, targetCode) || checkCodePairExist(targetCode, baseCode))
                throw new CodePairIsAlreadyExistException();

            Currencies baseCurrencies = currenciesService.findByCode(baseCode);
            Currencies targetCurrencies = currenciesService.findByCode(targetCode);
            Double rate = rateDTO.getRate();

            exchangeRateRepository.save(new ExchangeRate(baseCurrencies,targetCurrencies,rate));
        }

        if (!currenciesService.checkExistByCode(baseCode) && !currenciesService.checkExistByCode(targetCode))
            throw new BothCurrenciesNotFoundException();

        if(!currenciesService.checkExistByCode(baseCode))
            throw new BaseCurrenciesIsNotExistException();

        if (!currenciesService.checkExistByCode(targetCode))
            throw new TargetCurrenciesIsNotExistException();
    }

    @Transactional
    public void delete(String codePair) {
        exchangeRateRepository.delete(findByCodePair(codePair));
    }

    public ExchangeRate findByBaseTargetCodes (String baseCode, String targetCode){
        return exchangeRateRepository.findExchangeRateByBaseCurrencyId_CodeAndTargetCurrencyId_Code(baseCode, targetCode);
    }

    public boolean checkCodePairExist (String baseCode, String targetCode) {
        return findByBaseTargetCodes(baseCode, targetCode) != null;
    }

    private ExchangeRateDTO convertToExchangeRateDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
    }

    private String splitCodePair (String codePair, int codeIndex){
        String[] split = codePair.split("");
        StringBuilder baseCode = new StringBuilder(), targetCode = new StringBuilder();
        List<String> codes = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            if (i<3) baseCode.append(split[i]);
            else targetCode.append(split[i]);
        }
        codes.add(String.valueOf(baseCode));
        codes.add(String.valueOf(targetCode));

        return codes.get(codeIndex);
    }





}
