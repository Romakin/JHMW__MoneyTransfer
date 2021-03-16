package org.home.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dao.Operation;
import org.home.MoneyTransfer.dao.OperationStatus;
import org.home.MoneyTransfer.dao.PayCard;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dto.TransferRequestAmount;
import org.home.MoneyTransfer.repository.OperationReporitory;
import org.home.MoneyTransfer.repository.PayCardRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Service
public class TransferService {

    private PayCardRepository payCardRepository;
    private OperationReporitory operationReporitory;
    private HashService hash;
    private VerificationService verification;
    private CurrencyRateService currencyRate;

    /**
     * Creating operation and sen send verification code
     * @param request
     * @return operationId
     */
    public String transfer(TransferRequest request) {
        String verificationCode = verification.generateVerificationCode();
        PayCard cardFrom = payCardRepository.findByCardNumber(request.getCardFromNumber()).get();
        try {
            Operation o = operationReporitory.saveAndFlush(
                    Operation.builder()
                            .payCardFrom(cardFrom)
                            .payCardTo(payCardRepository.findByCardNumber(request.getCardToNumber()).get())
                            .amountCurrency(request.getAmount().getCurrency())
                            .amountValue(request.getAmount().getValue())
                            .operationStatus(OperationStatus.PREPARED)
                            .verificationHash(hash.getMD5(verificationCode))
                            .build()
            );
            verification.sendVerificationCode(verificationCode, cardFrom);
            return o.getOperationId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validaion of request
     * @param request
     * @return
     */
    public boolean checkValidRequest(TransferRequest request) {
        PayCard payCard = payCardRepository.findByCardNumber(request.getCardFromNumber()).get();
        return (
                payCard != null &&
                        payCardRepository.findByCardNumber(request.getCardToNumber()).isPresent() &&
                        payCard.getCVVHash().equals(hash.getMD5(request.getCardFromCVV())) &&
                        payCard.getTillDate().equals(request.getCardFromValidTill()) &&
                        Currency.valueWithLabel(request.getAmount().getCurrency()) != null &&
                        checkValidDate(payCard.getTillDate()) &&
                        checkAvailableBalance(request.getAmount(), payCard)
        );
    }

    /**
     * Validate tillDate
     * @param tillDate
     * @return
     */
    private boolean checkValidDate(String tillDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth date = YearMonth.parse(tillDate, formatter);
        return date.isAfter(YearMonth.now());
    }

    /**
     * Check avalible card balance with currencies
     * @param amount
     * @param payCard
     * @return
     */
    private boolean checkAvailableBalance(TransferRequestAmount amount, PayCard payCard) {
        double balanceVal = currencyRate.exchange(
                payCard.getBalance(),
                payCard.getCurrency(),
                Currency.valueWithLabel(amount.getCurrency()));
        return balanceVal - amount.getValue() > 0;
    }
}
