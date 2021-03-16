package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.PayCard;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    /**
     * Sending verfication code
     * ToDo call remote service to send to user
     * @param code
     * @param card
     * @return
     */
    public boolean sendVerificationCode(String code, PayCard card) {
        System.out.println("# CODE " + code + " \n#   FOR CARD " + card);
        return false;
    }

    /**
     * Generate verification code with timeMillis (simple mode)
     * @return verification code
     */
    public String generateVerificationCode() {
        String buff = String.format("%f", System.currentTimeMillis() - Math.random()).replaceAll("[^0-9]*", "");
        return buff.substring(buff.length() - 7, buff.length() -1);
    }

}
