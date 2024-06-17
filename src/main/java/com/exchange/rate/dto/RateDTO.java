package com.exchange.rate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RateDTO {

    @NotBlank
    @Size(min = 3, max = 3, message = "code size should be 3 symbols")
    private String baseCurrenciesCode;

    @NotBlank
    @Size(min = 3, max = 3, message = "code size should be 3 symbols")
    private String targetCurrenciesCode;

    @Positive
    @NotNull
    private Double rate;

}
