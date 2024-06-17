package com.exchange.rate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeRateDTO {

    @NotNull(message = "Exchange rate base currency shouldn't be empty")
    private CurrenciesDTO baseCurrency;

    @NotNull(message = "Exchange rate target currency shouldn't be empty")
    private CurrenciesDTO targetCurrency;

    @NotNull(message = "Exchange rate shouldn't be empty")
    private Double rate;


}
