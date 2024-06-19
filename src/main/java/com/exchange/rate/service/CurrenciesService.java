package com.exchange.rate.service;

import com.exchange.rate.dto.AllCurrenciesDto;
import com.exchange.rate.dto.CurrenciesDto;
import com.exchange.rate.model.Currencies;
import com.exchange.rate.repository.CurrenciesRepository;
import com.exchange.rate.util.customException.ThisObjectIsAlreadyExist;
import com.exchange.rate.util.customException.NoDataException;
import com.exchange.rate.util.customException.ThisObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CurrenciesService {

private final CurrenciesRepository currenciesRepository;
private final ModelMapper modelMapper;

    @Autowired
    public CurrenciesService(CurrenciesRepository currenciesRepository,  ModelMapper modelMapper) {
        this.currenciesRepository = currenciesRepository;
        this.modelMapper = modelMapper;
    }

    public AllCurrenciesDto findAll() {
        List<Currencies> allCurrencies = currenciesRepository.findAll();
        if (allCurrencies.isEmpty())
            throw new NoDataException();

        List<CurrenciesDto> allCurrenciesDTO = allCurrencies.stream().map(this::convertToCurrenciesDTO).collect(Collectors.toList());

        return new AllCurrenciesDto(allCurrenciesDTO);
    }

    public Optional<Currencies> findByCode(String code) {

        var currency = currenciesRepository.findByCode(code);

        if (currency.isEmpty())
            throw new ThisObjectNotFoundException();

        return currency;
    }


    @Transactional
    public void save (CurrenciesDto currenciesDTO) {

        if (currenciesRepository.findByCode(currenciesDTO.getCode()).isPresent())
            throw new ThisObjectIsAlreadyExist();

        currenciesRepository.save(convertToCurrencies(currenciesDTO));
    }

    @Transactional
    public void delete(String code) {
        currenciesRepository.delete(findByCode(code).get());
    }

    public CurrenciesDto convertToCurrenciesDTO (Currencies currencies) {
        return modelMapper.map(currencies, CurrenciesDto.class);
    }

    private Currencies convertToCurrencies(CurrenciesDto currenciesDTO) {
        return modelMapper.map(currenciesDTO, Currencies.class);
    }


}
