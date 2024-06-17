package com.exchange.rate.controller;

import com.exchange.rate.dto.AllExchangeRateDTO;
import com.exchange.rate.dto.ExchangeRateDTO;
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
    public AllExchangeRateDTO findAll(){
        return exchangeRateService.findAll();
    }

    @GetMapping("/findByBaseCode/{code}")
    public AllExchangeRateDTO findByBaseCode(@PathVariable(name = "code") String code){
        return exchangeRateService.findByBaseCode(code);
    }

    @GetMapping("/findByCodePair/{codePair}")
    public ExchangeRateDTO findByCodePair (@PathVariable(name = "codePair") String codePair){
        return exchangeRateService.findDTOByCodePair(codePair);
    }

    @PatchMapping
    public ExchangeRateDTO update (@Valid @RequestBody RateDTO rateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new RateIsEmptyException(errorMsgBuilder.errorMsg(bindingResult));

        exchangeRateService.update(rateDTO);
        String codePair = rateDTO.getBaseCurrenciesCode()+rateDTO.getTargetCurrenciesCode();
        return exchangeRateService.findDTOByCodePair(codePair);
    }

    @DeleteMapping("/{codePair}")
    public ResponseEntity<HttpStatus> delete (@PathVariable(name = "codePair") String codePair){
        exchangeRateService.delete(codePair);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ExchangeRateDTO save (@Valid @RequestBody RateDTO rateDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            throw new ExchangeRateNotValidException(errorMsgBuilder.errorMsg(bindingResult));

        exchangeRateService.save(rateDTO);
        String codePair = rateDTO.getBaseCurrenciesCode()+rateDTO.getTargetCurrenciesCode();
        return exchangeRateService.findDTOByCodePair(codePair);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(NoDataException e){
        AllErrorResponse response = new AllErrorResponse(
                "There is no data about exchange rates", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ExchangeRateWithCurrentBaseCodeNotExist e){
        AllErrorResponse response = new AllErrorResponse(
                "Exchange rate with current Base code doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CurrenciesIsIdenticalException e){
        AllErrorResponse response = new AllErrorResponse(
                "You can't save two identical currencies", LocalDateTime.now());
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

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(BothCurrenciesNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
                "These currencies doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(BaseCurrenciesIsNotExistException e){
        AllErrorResponse response = new AllErrorResponse(
                "Base currency doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(TargetCurrenciesIsNotExistException e){
        AllErrorResponse response = new AllErrorResponse(
                "Target currency doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
