package org.home.MoneyTransfer.repository;

import org.home.MoneyTransfer.dao.PayCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayCardRepository extends JpaRepository<PayCard, String> {

    @Override
    Optional<PayCard> findById(String s);

    Optional<PayCard> findByCardNumber(String n);
}
