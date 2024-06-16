package com.exchange.rate.controller;

import com.exchange.rate.dto.ExchangeRateDTO;
import com.exchange.rate.dto.ExchangeResponse;
import com.exchange.rate.dto.RateDTO;
import com.exchange.rate.services.ExchangeRateService;
import com.exchange.rate.util.AllErrorResponse;
import com.exchange.rate.util.ErrorMessageBuilder;
import com.exchange.rate.util.customExceptions.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/exchangeRate")
public class ExchangeRateController {

   private final ExchangeRateService exchangeRateService;
   private final ErrorMessageBuilder errorMsgBuilder;


    public ExchangeRateController(ExchangeRateService exchangeRateService, ErrorMessageBuilder errorMessageBuilder) {
        this.exchangeRateService = exchangeRateService;
        this.errorMsgBuilder = errorMessageBuilder;
    }

    @GetMapping
    public ExchangeResponse getExchangeRate(){
        return new ExchangeResponse(exchangeRateService.findAll());
    }

    @GetMapping("/findByBaseCode/{code}")
    public ExchangeResponse getExchangeRateByBaseCurrencies(@PathVariable(name = "code") String code){
        return new ExchangeResponse(exchangeRateService.findByBaseCurrenciesCode(code));
    }

    @GetMapping("/findByCodePair/{codePair}")
    public ExchangeRateDTO getExchangeRateByCodePair(@PathVariable(name = "codePair") String codePair){
        return exchangeRateService.findByCodePair(codePair);
    }

    @PatchMapping("/{codePair}")
    public ExchangeRateDTO update (@PathVariable(name = "codePair") String codePair,
                                   @Valid @RequestBody RateDTO rate, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new RateIsEmptyException(errorMsgBuilder.errorMsg(bindingResult));

        exchangeRateService.update(codePair, rate);
        return exchangeRateService.findByCodePair(codePair);
    }

    @DeleteMapping("/delete/{codePair}")
    public ResponseEntity<HttpStatus> delete (@PathVariable(name = "codePair") String codePair){
        exchangeRateService.delete(codePair);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save (@Valid @RequestBody ExchangeRateDTO exchangeRateDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            throw new ExchangeRateNotValidException(errorMsgBuilder.errorMsg(bindingResult));

        exchangeRateService.save(exchangeRateDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ExchangeRateWithCurrentBaseCodeNotExist e){
        AllErrorResponse response = new AllErrorResponse(
                "Exchange rate with current Base code doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CodePairNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
                "Exchange rate with current code pair doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ExchangeRateNotValidException e){
        AllErrorResponse response = new AllErrorResponse(
                e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CodePairIsAlreadyExistException e){
        AllErrorResponse response = new AllErrorResponse(
                "Exchange rate with this code pair is already exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(RateIsEmptyException e){
        AllErrorResponse response = new AllErrorResponse(
                e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
