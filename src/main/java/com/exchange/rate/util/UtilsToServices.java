package com.exchange.rate.util;

import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.dto.ExchangeRateDTO;
import com.exchange.rate.models.Currencies;
import com.exchange.rate.models.ExchangeRate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UtilsToServices {

    private final ModelMapper modelMapper;

    public UtilsToServices(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CurrenciesDTO convertToCurrenciesDTO (Currencies currencies) {
        return modelMapper.map(currencies, CurrenciesDTO.class);
    }

    public Currencies convertToCurrencies(CurrenciesDTO currenciesDTO) {
        return modelMapper.map(currenciesDTO, Currencies.class);
    }

    public ExchangeRateDTO convertToExchangeRateDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
    }

    public ExchangeRate convertToExchangeRate(ExchangeRateDTO exchangeRateDTO) {
        return modelMapper.map(exchangeRateDTO, ExchangeRate.class);
    }



    public String splitCodePair (String codePair, int codeIndex){
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
