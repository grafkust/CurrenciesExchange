package com.exchange.rate.controller;

import com.exchange.rate.customInterface.ExchangeRateExceptionHandler;
import com.exchange.rate.dto.AllExchangeRateDto;
import com.exchange.rate.dto.ExchangeRateDto;
import com.exchange.rate.dto.RateDto;
import com.exchange.rate.service.ExchangeRateService;
import com.exchange.rate.util.ErrorMessageBuilder;
import com.exchange.rate.util.customException.NotValidRequestException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchangeRate")
@ExchangeRateExceptionHandler
public class ExchangeRateController {

   private final ExchangeRateService exchangeRateService;
   private final ErrorMessageBuilder errorMsgBuilder;

    public ExchangeRateController(ExchangeRateService exchangeRateService, ErrorMessageBuilder errorMessageBuilder) {
        this.exchangeRateService = exchangeRateService;
        this.errorMsgBuilder = errorMessageBuilder;
    }

    @GetMapping
    public AllExchangeRateDto findAll(){
        return exchangeRateService.findAll();
    }

    @GetMapping("/findByBaseCode/{code}")
    public AllExchangeRateDto findByBaseCode(@PathVariable(name = "code") String code){
        return exchangeRateService.findByBaseCode(code);
    }

    @GetMapping("/findByCodePair/{codePair}")
    public ExchangeRateDto findByCodePair (@PathVariable(name = "codePair") String codePair){
        return exchangeRateService.convertToExchangeRateDTO(exchangeRateService.findByCodePair(codePair));
    }

    @PatchMapping
    public ExchangeRateDto update (@Valid @RequestBody RateDto rateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new NotValidRequestException(errorMsgBuilder.errorMsg(bindingResult));

        exchangeRateService.update(rateDTO);
        String codePair = rateDTO.getBaseCurrenciesCode()+rateDTO.getTargetCurrenciesCode();
        return exchangeRateService.convertToExchangeRateDTO(exchangeRateService.findByCodePair(codePair));
    }

    @DeleteMapping("/{codePair}")
    public ResponseEntity<HttpStatus> delete (@PathVariable(name = "codePair") String codePair){
        exchangeRateService.delete(codePair);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ExchangeRateDto save (@Valid @RequestBody RateDto rateDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            throw new NotValidRequestException(errorMsgBuilder.errorMsg(bindingResult));

        exchangeRateService.save(rateDTO);
        String codePair = rateDTO.getBaseCurrenciesCode()+rateDTO.getTargetCurrenciesCode();
        return exchangeRateService.convertToExchangeRateDTO(exchangeRateService.findByCodePair(codePair));
    }




}
