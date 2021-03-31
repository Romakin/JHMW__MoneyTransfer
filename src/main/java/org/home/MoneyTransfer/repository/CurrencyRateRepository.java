package org.home.MoneyTransfer.repository;

import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRateRepository  extends JpaRepository<CurrencyRate, String> {

    Optional<CurrencyRate> findByCurrencyFromAndCurrencyTo(Currency from, Currency to);
}
