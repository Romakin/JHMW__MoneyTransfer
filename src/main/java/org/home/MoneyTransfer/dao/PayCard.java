package org.home.MoneyTransfer.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PayCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String payCardId;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String tillDate;

    @Column(nullable = false)
    private String CVVHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private double balance;

}
