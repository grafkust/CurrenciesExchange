package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AllExchangeRateDto {

    private List<ExchangeRateDto> AllExchangeRate;

    public AllExchangeRateDto(List<ExchangeRateDto> exchange) {
        this.AllExchangeRate = exchange;
    }
}
