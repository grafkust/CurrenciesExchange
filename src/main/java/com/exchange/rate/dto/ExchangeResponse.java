package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ExchangeResponse {

    private List<ExchangeRateDTO> exchangeRate;

    public ExchangeResponse(List<ExchangeRateDTO> exchange) {
        this.exchangeRate = exchange;
    }
}
