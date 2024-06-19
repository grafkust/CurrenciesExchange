package com.exchange.rate.service;

import com.exchange.rate.dto.AllExchangeRateDto;
import com.exchange.rate.dto.ExchangeRateDto;
import com.exchange.rate.dto.RateDto;
import com.exchange.rate.model.Currencies;
import com.exchange.rate.model.ExchangeRate;
import com.exchange.rate.repository.ExchangeRateRepository;
import com.exchange.rate.util.customException.ThisObjectIsAlreadyExist;
import com.exchange.rate.util.customException.CurrenciesIsIdenticalException;
import com.exchange.rate.util.customException.NoDataException;
import com.exchange.rate.util.customException.ThisObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public AllExchangeRateDto findAll() {
        List<ExchangeRate> allExchangeRate = exchangeRateRepository.findAll();

        if (allExchangeRate.isEmpty())
            throw new NoDataException();

        List<ExchangeRateDto> allExchangeRateDTO = allExchangeRate.stream().map(this::convertToExchangeRateDTO).collect(Collectors.toList());
        return new AllExchangeRateDto(allExchangeRateDTO);
    }

    public AllExchangeRateDto findByBaseCode(String code) {
        List<ExchangeRate> baseCurrency = exchangeRateRepository.findByBaseCurrencyId_Code(code);

        if (baseCurrency.isEmpty())
            throw new ThisObjectNotFoundException();

        List<ExchangeRateDto> allExchangeRateDTO = baseCurrency.stream().map(this::convertToExchangeRateDTO).collect(Collectors.toList());
        return new AllExchangeRateDto(allExchangeRateDTO);
    }
    public ExchangeRate findByCodePair(String codePair){

        String baseCode = splitCodePair(codePair, 0);
        String targetCode = splitCodePair(codePair, 1);

        Optional <ExchangeRate> exchangeRate = findByBaseTargetCodes(baseCode,targetCode);
        if (exchangeRate.isEmpty())
            throw new ThisObjectNotFoundException();

        return exchangeRate.get();
    }

    @Transactional
    public void update(RateDto newRate){
        ExchangeRate exchangeRateToUpdate = findByCodePair(
                newRate.getBaseCurrenciesCode() + newRate.getTargetCurrenciesCode());

        exchangeRateToUpdate.setRate(newRate.getRate());
    }

    @Transactional
    public void save(RateDto rateDTO) {

        String baseCode = rateDTO.getBaseCurrenciesCode();
        String targetCode = rateDTO.getTargetCurrenciesCode();

        if (baseCode.equals(targetCode))
            throw new CurrenciesIsIdenticalException();

        if (findByBaseTargetCodes(baseCode, targetCode).isPresent() || findByBaseTargetCodes(targetCode, baseCode).isPresent())
            throw new ThisObjectIsAlreadyExist();

        Currencies baseCurrency = currenciesService.findByCode(baseCode).get();
        Currencies targetCurrency = currenciesService.findByCode(targetCode).get();
        Double rate = rateDTO.getRate();

        exchangeRateRepository.save(new ExchangeRate(baseCurrency,targetCurrency,rate));

    }

    @Transactional
    public void delete(String codePair) {
        exchangeRateRepository.delete(findByCodePair(codePair));
    }

    public Optional<ExchangeRate> findByBaseTargetCodes (String baseCode, String targetCode){
        return exchangeRateRepository.findExchangeRateByBaseCurrencyId_CodeAndTargetCurrencyId_Code(baseCode, targetCode);
    }

    public ExchangeRateDto convertToExchangeRateDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDto.class);
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
