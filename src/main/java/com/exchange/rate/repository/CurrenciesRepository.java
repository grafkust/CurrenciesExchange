package com.exchange.rate.repository;

import com.exchange.rate.model.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrenciesRepository extends JpaRepository<Currencies, Integer> {

    Optional<Currencies> findByCode(String code);


}
