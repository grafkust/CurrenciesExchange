package com.exchange.rate.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@NotNull
public class CurrenciesDto {

    @NotBlank
    @Size(min = 3, max = 3, message = "code size should be 3 symbols")
    private String code;

    @NotBlank
    private String fullName;

    @NotBlank
    @Size(min = 1, max = 3, message = "sign size should be  1 - 3 symbols")
    private String sign;


}
