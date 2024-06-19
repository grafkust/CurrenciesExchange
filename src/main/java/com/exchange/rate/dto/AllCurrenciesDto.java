package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllCurrenciesDto {

    private List<CurrenciesDto> allCurrencies;

    public AllCurrenciesDto(List<CurrenciesDto> currencies) {
        this.allCurrencies = currencies;
    }
}
