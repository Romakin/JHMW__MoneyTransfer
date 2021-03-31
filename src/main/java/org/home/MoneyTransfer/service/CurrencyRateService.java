package org.home.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.CurrencyRate;
import org.home.MoneyTransfer.repository.CurrencyRateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CurrencyRateService {

    private CurrencyRateRepository currencyRateRepository;

    /**
     * Exchange money in normal mode
     * RUR - неверный код! Правильный RUB! (пояснение в Currency)
     * @param sumFrom
     * @param currencyFrom
     * @param currencyTo
     * @return exchangeSum
     */
    public double exchange(double sumFrom, Currency currencyFrom, Currency currencyTo) {
        double val = sumFrom * getRateIndex(currencyFrom, currencyTo) * 10000;
        val = Math.round(val);
        val = val / 10000;
        return val;
    }

    /**
     * Get Index for exchange money
     * ToDo Call remote service for getting index
     * @param currencyFrom
     * @param currencyTo
     * @return rateIndex
     */
    public double getRateIndex(Currency currencyFrom, Currency currencyTo) {
        Optional<CurrencyRate> currencyRateOptional =
                currencyRateRepository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);
        if (currencyRateOptional.isPresent()) {
            return currencyRateOptional.get().getRateIndex();
        }
        return 1;
    }
}
