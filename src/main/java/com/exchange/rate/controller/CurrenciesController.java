package com.exchange.rate.controller;

import com.exchange.rate.dto.AllCurrenciesDTO;
import com.exchange.rate.dto.CurrenciesDTO;
import com.exchange.rate.services.CurrenciesService;
import com.exchange.rate.util.AllErrorResponse;
import com.exchange.rate.util.ErrorMessageBuilder;
import com.exchange.rate.util.customExceptions.CodeNotFoundException;
import com.exchange.rate.util.customExceptions.CurrenciesIsAlreadyExist;
import com.exchange.rate.util.customExceptions.CurrenciesNotValidException;
import com.exchange.rate.util.customExceptions.NoDataException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/currencies")
public class CurrenciesController {

    private final CurrenciesService currenciesService;
    private final ErrorMessageBuilder errorMsgBuilder;

    public CurrenciesController(CurrenciesService currenciesService, ErrorMessageBuilder errorMsgBuilder) {
        this.currenciesService = currenciesService;
        this.errorMsgBuilder = errorMsgBuilder;
    }

    @GetMapping
    public AllCurrenciesDTO findAll() {
        return currenciesService.findAll();
    }

    @GetMapping("/{code}")
    public CurrenciesDTO findByCode(@PathVariable(name = "code") String code) {
        return currenciesService.findDTOByCode(code);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save (@Valid @RequestBody CurrenciesDTO currenciesDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new CurrenciesNotValidException(errorMsgBuilder.errorMsg(bindingResult));
        currenciesService.save(currenciesDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "code") String code){
        currenciesService.delete(code);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(NoDataException e){
        AllErrorResponse response = new AllErrorResponse(
                "There is no data about currencies", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CodeNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
            "This code doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CurrenciesIsAlreadyExist e){
        AllErrorResponse response = new AllErrorResponse(
                "This currencies is already exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CurrenciesNotValidException e){
        AllErrorResponse response = new AllErrorResponse(
                e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    
}
