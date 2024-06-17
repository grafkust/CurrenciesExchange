package com.exchange.rate.services;

import com.exchange.rate.dto.AllCurrenciesDTO;
import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.models.Currencies;
import com.exchange.rate.repositories.CurrenciesRepository;
import com.exchange.rate.util.customExceptions.CodeNotFoundException;
import com.exchange.rate.util.customExceptions.CurrenciesIsAlreadyExist;
import com.exchange.rate.util.customExceptions.NoDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public AllCurrenciesDTO findAll() {
        List<Currencies> allCurrencies = currenciesRepository.findAll();
        if (allCurrencies.isEmpty())
            throw new NoDataException();

        List<CurrenciesDTO> allCurrenciesDTO = allCurrencies.stream().map(this::convertToCurrenciesDTO).collect(Collectors.toList());

        return new AllCurrenciesDTO(allCurrenciesDTO);
    }

    public CurrenciesDTO findDTOByCode(String code) {

        return convertToCurrenciesDTO(findByCode(code));
    }

    public Currencies findByCode(String code) {

        if (!checkExistByCode(code))
            throw new CodeNotFoundException();

        return currenciesRepository.findByCode(code);
    }

    @Transactional
    public void save (CurrenciesDTO currenciesDTO) {

        if (checkExistByCode(currenciesDTO.getCode()))
            throw new CurrenciesIsAlreadyExist();

        currenciesRepository.save(convertToCurrencies(currenciesDTO));
    }

    @Transactional
    public void delete(String code) {
        currenciesRepository.delete(findByCode(code));
    }

    public boolean checkExistByCode (String code){
        return currenciesRepository.findByCode(code) != null;
    }

    private CurrenciesDTO convertToCurrenciesDTO (Currencies currencies) {
        return modelMapper.map(currencies, CurrenciesDTO.class);
    }

    public Currencies convertToCurrencies(CurrenciesDTO currenciesDTO) {
        return modelMapper.map(currenciesDTO, Currencies.class);
    }


}
