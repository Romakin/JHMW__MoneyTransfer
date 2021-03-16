package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.Currency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyRateService {

    /**
     * Exchange money in normal mode
     * RUR - неверный код! Правильный RUB! (пояснение в Currency)
     * @param sumFrom
     * @param currencyFrom
     * @param currencyTo
     * @return exchangeSum
     */
    public double exchange(double sumFrom, Currency currencyFrom, Currency currencyTo) {
        return sumFrom * getRateIndex(currencyFrom, currencyTo);
    }

    /**
     * Get Index for exchange money
     * ToDo Call remote service for getting index
     * @param currencyFrom
     * @param currencyTo
     * @return rateIndex
     */
    public double getRateIndex(Currency currencyFrom, Currency currencyTo) {
        switch (currencyFrom) {
            case EUR:
                switch (currencyTo) {
                    case RUR: return 86.75;
                    case USD: return 1.16;
                }
            case RUR:
                switch (currencyTo) {
                    case EUR: return 1/90.4;
                    case USD: return 1/75.5;
                }
                break;
            case USD:
                switch (currencyTo) {
                    case RUR: return 71.85;
                    case EUR: return 1/1.24;
                }
                break;
        }
        return 1;
    }
}
