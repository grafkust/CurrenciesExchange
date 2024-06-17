package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllCurrenciesDTO {

    private List<CurrenciesDTO> allCurrencies;

    public AllCurrenciesDTO(List<CurrenciesDTO> currencies) {
        this.allCurrencies = currencies;
    }
}
