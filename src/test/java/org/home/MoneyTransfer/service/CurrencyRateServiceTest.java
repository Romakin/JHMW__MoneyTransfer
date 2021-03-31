package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyRateServiceTest {

    // тестирование конвертации

    @Autowired
    CurrencyRateService currencyRateService;

    @Test
    void exchange() {

        double res = currencyRateService.exchange(10.00, Currency.EUR, Currency.RUR);
        double res1 = currencyRateService.exchange(10.00, Currency.EUR, Currency.USD);
        double res2 = currencyRateService.exchange(90.40, Currency.RUR, Currency.EUR);
        double res3 = currencyRateService.exchange(75.50, Currency.RUR, Currency.USD);
        double res4 = currencyRateService.exchange(10.00, Currency.USD, Currency.RUR);
        double res5 = currencyRateService.exchange(1.24, Currency.USD, Currency.EUR);
        double res6 = currencyRateService.exchange(10.00, Currency.USD, Currency.USD);

        Assertions.assertEquals(res, 867.50);
        Assertions.assertEquals(res1, 11.60);
        Assertions.assertEquals(res2, 0.9944);
        Assertions.assertEquals(res3, 0.9815);
        Assertions.assertEquals(res4, 718.50);
        Assertions.assertEquals(res5, 0.9994);
        Assertions.assertEquals(res6, 10.00);

    }
}