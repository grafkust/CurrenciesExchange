package com.exchange.rate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RateDTO {

    @NotNull(message = "rate shouldn't be null")
    private Double rate;


}
