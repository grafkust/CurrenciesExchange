package com.exchange.rate.controller;

import com.exchange.rate.customInterface.CurrenciesExceptionHandler;
import com.exchange.rate.dto.AllCurrenciesDto;
import com.exchange.rate.dto.CurrenciesDto;
import com.exchange.rate.service.CurrenciesService;
import com.exchange.rate.util.ErrorMessageBuilder;
import com.exchange.rate.util.customException.NotValidRequestException;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currencies")
@CurrenciesExceptionHandler
public class CurrenciesController {

    private final CurrenciesService currenciesService;
    private final ErrorMessageBuilder errorMsgBuilder;

    public CurrenciesController(CurrenciesService currenciesService, ErrorMessageBuilder errorMsgBuilder) {
        this.currenciesService = currenciesService;
        this.errorMsgBuilder = errorMsgBuilder;
    }

    @GetMapping
    public AllCurrenciesDto findAll() {
        return currenciesService.findAll();
    }

    @GetMapping("/{code}")
    public CurrenciesDto findByCode(@PathVariable(name = "code") String code) {
        return currenciesService.convertToCurrenciesDTO(currenciesService.findByCode(code).get());
    }

    @PostMapping
    public void save (@Valid @RequestBody CurrenciesDto currenciesDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new NotValidRequestException(errorMsgBuilder.errorMsg(bindingResult));
        currenciesService.save(currenciesDTO);
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable(name = "code") String code){
        currenciesService.delete(code);
    }




    
}
