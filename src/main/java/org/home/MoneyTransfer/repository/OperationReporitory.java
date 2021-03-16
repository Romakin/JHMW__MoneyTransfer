package org.home.MoneyTransfer.repository;

import org.home.MoneyTransfer.dao.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationReporitory extends JpaRepository<Operation, String> {

}
