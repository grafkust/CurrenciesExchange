package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CurrenciesResponse {

    private List<CurrenciesDTO> currencies;

    public CurrenciesResponse(List<CurrenciesDTO> currencies) {
        this.currencies = currencies;
    }
}
