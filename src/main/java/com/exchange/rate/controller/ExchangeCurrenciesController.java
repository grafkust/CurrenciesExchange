package com.exchange.rate.controller;

import com.exchange.rate.customInterface.CurrenciesExceptionHandler;
import com.exchange.rate.customInterface.ExchangeRateExceptionHandler;
import com.exchange.rate.dto.ExchangeCurrenciesDto;
import com.exchange.rate.service.ExchangeCurrenciesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/exchange")
@CurrenciesExceptionHandler
@ExchangeRateExceptionHandler
public class ExchangeCurrenciesController {

    private final ExchangeCurrenciesService exchangeCurrenciesService;

    public ExchangeCurrenciesController(ExchangeCurrenciesService exchangeCurrenciesService) {
        this.exchangeCurrenciesService = exchangeCurrenciesService;
    }

    @GetMapping
    public ExchangeCurrenciesDto exchange(@RequestParam Map<String,String> params) {

        String baseCode = params.get("from");
        String targetCode = params.get("to");
        Double amount = Double.valueOf(params.get("amount"));

       return exchangeCurrenciesService.exchange(baseCode, targetCode, amount);
    }










}
