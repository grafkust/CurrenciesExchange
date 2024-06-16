package com.exchange.rate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeDTO {

    private CurrenciesDTO baseCurrency;

    private CurrenciesDTO targetCurrency;

    private Double rate;

    private Double amount;

    private Double convertToAmount;

    public ExchangeDTO(CurrenciesDTO baseCurrency, CurrenciesDTO targetCurrency, Double rate, Double amount, Double convertToAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertToAmount = convertToAmount;
    }

}
