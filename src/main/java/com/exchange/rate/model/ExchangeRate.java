package com.exchange.rate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "Rate")
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Base", referencedColumnName = "ID")
    private Currencies baseCurrency;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Target", referencedColumnName = "ID")
    private Currencies targetCurrency;

    @Column(name = "Rate")
    private Double rate;

    public ExchangeRate(Currencies baseCurrencyId, Currencies targetCurrencyId, Double rate) {
        this.baseCurrency = baseCurrencyId;
        this.targetCurrency = targetCurrencyId;
        this.rate = rate;
    }


}
