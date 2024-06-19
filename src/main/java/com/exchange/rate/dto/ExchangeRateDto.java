package com.exchange.rate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeRateDto {

    @NotNull(message = "Exchange rate base currency shouldn't be empty")
    private CurrenciesDto baseCurrency;

    @NotNull(message = "Exchange rate target currency shouldn't be empty")
    private CurrenciesDto targetCurrency;

    @NotNull(message = "Exchange rate shouldn't be empty")
    private Double rate;


}
