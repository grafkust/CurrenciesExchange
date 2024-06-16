package com.exchange.rate.controller;

import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.dto.CurrenciesResponse;
import com.exchange.rate.services.CurrenciesService;
import com.exchange.rate.util.AllErrorResponse;
import com.exchange.rate.util.customExceptions.CodeNotFoundException;
import com.exchange.rate.util.customExceptions.CurrenciesIsAlreadyExist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/currencies")
public class CurrenciesController {

    private final CurrenciesService currenciesService;

    public CurrenciesController(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @GetMapping
    public CurrenciesResponse getCurrencies() {
        return new CurrenciesResponse(currenciesService.findAll());
    }

    @GetMapping("/{code}")
    public CurrenciesDTO getCurrenciesByCode(@PathVariable(name = "code") String code) {
        return currenciesService.findByCode(code);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save (@RequestBody CurrenciesDTO currenciesDTO){
        currenciesService.save(currenciesDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/delete/{code}")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "code") String code){
        currenciesService.delete(code);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CodeNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
            "This code doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CurrenciesIsAlreadyExist e){
        AllErrorResponse response = new AllErrorResponse(
                "This currencies is already exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }






}
