package com.exchange.rate.services;

import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.models.Currencies;
import com.exchange.rate.repositories.CurrenciesRepository;
import com.exchange.rate.util.UtilsToServices;
import com.exchange.rate.util.customExceptions.CodeNotFoundException;
import com.exchange.rate.util.customExceptions.CurrenciesIsAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CurrenciesService {

private final CurrenciesRepository currenciesRepository;
private final UtilsToServices utilsToServices;

    @Autowired
    public CurrenciesService(CurrenciesRepository currenciesRepository, UtilsToServices utilsToServices) {
        this.currenciesRepository = currenciesRepository;
        this.utilsToServices = utilsToServices;
    }


    public List<CurrenciesDTO> findAll() {
        return currenciesRepository.findAll().stream().map(this :: convertToCurrenciesDTO).collect(Collectors.toList());
    }

    public CurrenciesDTO findByCode (String code) {
        checkExistByCode(code);

        return utilsToServices.convertToCurrenciesDTO(currenciesRepository.findByCode(code));
    }

    @Transactional
    public void save (CurrenciesDTO currenciesDTO) {
        if(currenciesRepository.findByCode(currenciesDTO.getCode()) != null)
            throw new CurrenciesIsAlreadyExist();
        currenciesRepository.save(utilsToServices.convertToCurrencies(currenciesDTO));
    }

    @Transactional
    public void delete(String code) {
        checkExistByCode(code);
        currenciesRepository.deleteByCode(code);
    }

    private void checkExistByCode (String code){
        if (currenciesRepository.findByCode(code) == null)
            throw new CodeNotFoundException();
    }

    private CurrenciesDTO convertToCurrenciesDTO (Currencies currencies) {
        return utilsToServices.convertToCurrenciesDTO(currencies);
    }


}
