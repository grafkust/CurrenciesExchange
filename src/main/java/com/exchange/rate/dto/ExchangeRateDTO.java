package com.exchange.rate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeRateDTO {

    @NotNull(message = "Exchange rate base currency shouldn't be empty")
    private CurrenciesDTO baseCurrencyDTO;

    @NotNull(message = "Exchange rate target currency shouldn't be empty")
    private CurrenciesDTO targetCurrencyDTO;

    @NotNull(message = "Exchange rate shouldn't be empty")
    private Double rate;


}
