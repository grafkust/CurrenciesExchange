package com.exchange.rate.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "Rate")
public class ExchangeRate {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Base", referencedColumnName = "ID")
    private Currencies baseCurrencyId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Target", referencedColumnName = "ID")
    private Currencies targetCurrencyId;

    @Column(name = "Rate")
    private Double rate;

    public ExchangeRate() {
    }

    public ExchangeRate( Currencies baseCurrencyId, Currencies targetCurrencyId, Double rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }
}
