package com.exchange.rate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeCurrenciesDTO {

    @NotNull
    private CurrenciesDTO baseCurrency;

    @NotNull
    private CurrenciesDTO targetCurrency;

    @NotNull
    private Double rate;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private Double convertToAmount;

    public ExchangeCurrenciesDTO(CurrenciesDTO baseCurrency, CurrenciesDTO targetCurrency, Double rate, Double amount, Double convertToAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertToAmount = convertToAmount;
    }

    public ExchangeCurrenciesDTO() {
    }
}
