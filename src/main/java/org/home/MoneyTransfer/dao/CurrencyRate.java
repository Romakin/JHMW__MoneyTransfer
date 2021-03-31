package org.home.MoneyTransfer.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class CurrencyRate {

    @Id
    @GeneratedValue
    private int rateId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currencyFrom;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currencyTo;

    @Column(nullable = false)
    private double rateIndex;

}
