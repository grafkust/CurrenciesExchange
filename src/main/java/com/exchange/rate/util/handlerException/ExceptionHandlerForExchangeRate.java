package com.exchange.rate.util.handlerException;

import com.exchange.rate.customInterface.ExchangeRateExceptionHandler;
import com.exchange.rate.util.AllErrorResponse;
import com.exchange.rate.util.customException.ThisObjectIsAlreadyExist;
import com.exchange.rate.util.customException.CurrenciesIsIdenticalException;
import com.exchange.rate.util.customException.ExchangeNotPossibleException;
import com.exchange.rate.util.customException.NoDataException;
import com.exchange.rate.util.customException.ThisObjectNotFoundException;
import com.exchange.rate.util.customException.NotValidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;


@ControllerAdvice(annotations = ExchangeRateExceptionHandler.class)
public class ExceptionHandlerForExchangeRate {


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleValidException(NotValidRequestException e){
        AllErrorResponse response = new AllErrorResponse(
                e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(NoDataException e){
        AllErrorResponse response = new AllErrorResponse(
                "There is no data about exchange rates", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(CurrenciesIsIdenticalException e){
        AllErrorResponse response = new AllErrorResponse(
                "You can't save two identical currencies", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ThisObjectNotFoundException e){
        AllErrorResponse response = new AllErrorResponse(
                "Exchange rate with current code pair doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ThisObjectIsAlreadyExist e){
        AllErrorResponse response = new AllErrorResponse(
                "This object is already exist", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AllErrorResponse> handleException(ExchangeNotPossibleException e){
        AllErrorResponse response = new AllErrorResponse(
                "Exchange of these currencies is not possible.", LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<AllErrorResponse> handleException(NoHandlerFoundException e) {
        AllErrorResponse response = new AllErrorResponse(
                e.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
