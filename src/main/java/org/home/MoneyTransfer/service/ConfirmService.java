package org.home.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.Operation;
import org.home.MoneyTransfer.dao.OperationStatus;
import org.home.MoneyTransfer.dao.PayCard;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.repository.OperationReporitory;
import org.home.MoneyTransfer.repository.PayCardRepository;
import org.home.MoneyTransfer.service.utils.HashService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmService {

    private PayCardRepository payCardRepository;
    private OperationReporitory operationReporitory;
    private CurrencyRateService currencyRate;

    /**
     * Set New Balances and confirm operation
     * ToDo - register exchange operations and calc Bank comissions
     * @param request
     * @return operationId
     */
    @Transactional
    public String confirm(ConfirmRequest request) {
        Operation operation = operationReporitory.findById(request.getOperationId()).get();
        PayCard payCardFrom = operation.getPayCardFrom(),
                payCardTo = operation.getPayCardTo();
        setNewBalanceToPayCardFrom(operation, payCardFrom);
        setNewBalanceToPayCardTo(operation, payCardTo);
        confirmOperationAndSaveAll(operation, payCardFrom, payCardTo);
        return operation.getOperationId();
    }

    /**
     * Update operation status and save operation and changed cards
     * @param operation
     * @param payCardFrom
     * @param payCardTo
     */
    private void confirmOperationAndSaveAll(Operation operation, PayCard payCardFrom, PayCard payCardTo) {
        operation.setOperationStatus(OperationStatus.SUCCESS);
        operation.setVerificationHash("_SUCCESS_");
        payCardRepository.save(payCardFrom);
        payCardRepository.save(payCardTo);
        payCardRepository.flush();
        operationReporitory.saveAndFlush(operation);
    }

    /**
     * Decrease card's balance with rate
     * @param operation
     * @param payCardTo
     */
    private void setNewBalanceToPayCardTo(Operation operation, PayCard payCardTo) {
        payCardTo.setBalance(
                payCardTo.getBalance() +
                        currencyRate.exchange(
                                operation.getAmountValue(),
                                payCardTo.getCurrency(),
                                Currency.valueWithLabel(operation.getAmountCurrency())
                        )
        );
    }

    /**
     * Decrease card's balance with rate
     * @param operation
     * @param payCardFrom
     */
    private void setNewBalanceToPayCardFrom(Operation operation, PayCard payCardFrom) {
        payCardFrom.setBalance(
                payCardFrom.getBalance() -
                        ( operation.getAmountValue() /
                            currencyRate.getRateIndex(
                                    payCardFrom.getCurrency(),
                                    Currency.valueWithLabel(operation.getAmountCurrency())
                            )
                        )
        );
    }

    /**
     * Validating verification code and operationId
     * @param request
     * @return
     */
    public boolean checkValidRequest(ConfirmRequest request) {
        Optional<Operation> operation = operationReporitory.findById(request.getOperationId());
        return  (
                operation.isPresent() &&
                        operation.get().getVerificationHash().equals(HashService.getMD5(request.getCode()))
        );
    }
}
