package com.exchange.rate.repositories;

import com.exchange.rate.models.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrenciesRepository extends JpaRepository<Currencies, Integer> {

    Currencies findByCode(String code);

    void deleteByCode(String code);

}
