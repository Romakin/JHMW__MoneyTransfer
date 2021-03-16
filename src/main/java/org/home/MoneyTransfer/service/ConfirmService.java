package org.home.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.Operation;
import org.home.MoneyTransfer.dao.OperationStatus;
import org.home.MoneyTransfer.dao.PayCard;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.repository.OperationReporitory;
import org.home.MoneyTransfer.repository.PayCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmService {

    private PayCardRepository payCardRepository;
    private OperationReporitory operationReporitory;
    private HashService hash;
    private CurrencyRateService currencyRate;

    /**
     * Set New Balances and confirm operation
     * ToDo - register exchange operations and calc Bank comissions
     * @param request
     * @return operationId
     */
    @Transactional
    public String confirm(ConfirmRequest request) {
        Operation o = operationReporitory.findById(request.getOperationId()).get();
        PayCard payCardFrom = o.getPayCardFrom(),
                payCardTo = o.getPayCardTo();
        payCardFrom.setBalance(
                payCardFrom.getBalance() -
                        ( o.getAmountValue() /
                            currencyRate.getRateIndex(
                                    payCardFrom.getCurrency(),
                                    Currency.valueWithLabel(o.getAmountCurrency())
                            )
                        )
        );
        payCardTo.setBalance(
                payCardTo.getBalance() +
                        currencyRate.exchange(
                                o.getAmountValue(),
                                payCardTo.getCurrency(),
                                Currency.valueWithLabel(o.getAmountCurrency())
                        )
        );
        o.setOperationStatus(OperationStatus.SUCCESS);
        o.setVerificationHash("_SUCCESS_");
        payCardRepository.save(payCardFrom);
        payCardRepository.save(payCardTo);
        payCardRepository.flush();
        operationReporitory.saveAndFlush(o);
        return o.getOperationId();
    }

    /**
     * Validating verification code and operationId
     * @param request
     * @return
     */
    public boolean checkValidRequest(ConfirmRequest request) {
        Optional<Operation> o = operationReporitory.findById(request.getOperationId());
        return  (
                o.isPresent() &&
                        o.get().getVerificationHash().equals(hash.getMD5(request.getCode()))
        );
    }
}
