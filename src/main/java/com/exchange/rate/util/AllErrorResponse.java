package com.exchange.rate.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AllErrorResponse {

    private String message;

    private LocalDateTime currentTime;

    public AllErrorResponse(String message, LocalDateTime currentTime) {
        this.message = message;
        this.currentTime = currentTime;
    }
}
