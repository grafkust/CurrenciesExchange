package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AllExchangeRateDTO {

    private List<ExchangeRateDTO> AllExchangeRate;

    public AllExchangeRateDTO(List<ExchangeRateDTO> exchange) {
        this.AllExchangeRate = exchange;
    }
}
