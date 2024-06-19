package com.exchange.rate.util.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotValidRequestException  extends RuntimeException{
    public NotValidRequestException(String msg){super(msg);}
}
