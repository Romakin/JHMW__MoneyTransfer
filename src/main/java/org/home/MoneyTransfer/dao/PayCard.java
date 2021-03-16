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
    String payCardId;

    @Column(nullable = false)
    String cardNumber;

    @Column(nullable = false)
    String tillDate;

    @Column(nullable = false)
    String CVVHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Currency currency;

    @Column(nullable = false)
    double balance;

}
