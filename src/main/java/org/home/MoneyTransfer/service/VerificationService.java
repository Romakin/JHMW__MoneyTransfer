package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.PayCard;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
        writeToFile("\n" + code);
        return false;
    }

    public void writeToFile(String code) {
        String path = "./gen/codes.txt";
        File lf = new File(path);
        if (!(lf.exists() && lf.canRead() && lf.canWrite())) {
            try {
                new File(lf.getParent()).mkdirs();
                if (lf.createNewFile()) {
                    System.out.println("File created");
                } else {
                    return;
                }
            } catch (IOException e) {
                System.out.println("saveToFile: " + e.getMessage());
                return;
            }
        }
        try {
            Files.write(Paths.get(lf.getAbsolutePath()), code.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("saveToFile: " + e.getMessage());
        }
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
