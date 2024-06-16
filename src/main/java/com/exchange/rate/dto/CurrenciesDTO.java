package com.exchange.rate.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CurrenciesDTO {


    @NotNull(message = "Currencies code shouldn't be empty")
    private String code;

    @NotNull(message = "Currencies full name shouldn't be empty")
    private String fullName;

    @NotNull(message = "Currencies sign shouldn't be empty")
    private String sign;


}
