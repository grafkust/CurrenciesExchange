package com.exchange.rate.util.handlerException;

import com.exchange.rate.customInterface.CurrenciesExceptionHandler;
import com.exchange.rate.util.AllErrorResponse;
import com.exchange.rate.util.customException.ThisObjectIsAlreadyExist;
import com.exchange.rate.util.customException.NoDataException;
import com.exchange.rate.util.customException.ThisObjectNotFoundException;
import com.exchange.rate.util.customException.NotValidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice(annotations = CurrenciesExceptionHandler.class)
public class ExceptionHandlerForCurrencies {


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ThisObjectNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
                "This code doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(NoDataException e){
        AllErrorResponse response = new AllErrorResponse(
                "There is no data about currencies", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ThisObjectIsAlreadyExist e){
        AllErrorResponse response = new AllErrorResponse(
                "This currency is already exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(NotValidRequestException e){
        AllErrorResponse response = new AllErrorResponse(
                e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
