package com.exchange.rate.controller;

import com.exchange.rate.dto.ExchangeDTO;
import com.exchange.rate.services.ExchangeCurrenciesService;
import com.exchange.rate.util.AllErrorResponse;
import com.exchange.rate.util.customExceptions.CodeNotFoundException;
import com.exchange.rate.util.customExceptions.CodePairNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/exchange")
public class ExchangeCurrenciesController {

    private final ExchangeCurrenciesService exchangeCurrenciesService;

    public ExchangeCurrenciesController(ExchangeCurrenciesService exchangeCurrenciesService) {
        this.exchangeCurrenciesService = exchangeCurrenciesService;
    }

    @GetMapping
    public ExchangeDTO exchange(@RequestParam Map<String,String> params) {

        String baseCode = params.get("from");
        String targetCode = params.get("to");
        Double amount = Double.valueOf(params.get("amount"));

       return exchangeCurrenciesService.exchange(baseCode, targetCode, amount);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CodeNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
                "This code doesn't exist", LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CodePairNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
                "Is not possible to exchange these currencies", LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }






}
