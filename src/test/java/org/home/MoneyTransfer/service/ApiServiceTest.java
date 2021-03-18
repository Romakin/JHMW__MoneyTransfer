package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.Operation;
import org.home.MoneyTransfer.dao.OperationStatus;
import org.home.MoneyTransfer.dao.PayCard;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dto.TransferRequestAmount;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ApiServiceTest {

    @Autowired
    private ApiService apiService;

    private List<PayCard> cards = Arrays.asList(new PayCard[]{
            new PayCard(
                    null, "1238923412356232",
                    "12/21", "99C5E07B4D5DE9D18C350CDF64C5AA3D",//567
                    Currency.valueOf("RUR"), 548.00
            ),
            new PayCard(
                    null, "1235823412340234",
                    "02/24", "A96B65A721E561E1E3DE768AC819FFBB", //409
                    Currency.valueOf("RUR"), 1000.00
            )
    });
    private String operId;

    @Test
    @Order(0)
    public void init() {
        apiService.getTransferService().getPayCardRepository().saveAndFlush(cards.get(0));
        apiService.getTransferService().getPayCardRepository().saveAndFlush(cards.get(1));

    }

    @Test
    @Order(1)
    void transfer() {
        TransferRequest request = new TransferRequest(
                cards.get(0).getCardNumber(), cards.get(0).getTillDate(), "567",
                cards.get(1).getCardNumber(),
                new TransferRequestAmount(20.00, "RUR")
        );
        ResponseEntity apiResp = apiService.transfer(request);
        operId = ( (HashMap<String, String>) apiResp.getBody()).get("operationID");

        System.out.println(apiResp.getBody());
        System.out.println(operId);

        Assertions.assertEquals(apiResp.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(operId.matches("OID_[0-9]+_P"));
    }

    @Test
    @Order(2)
    void confirm() {
        transfer();
        System.out.println(operId);
        ConfirmRequest confirmRequest = new ConfirmRequest(operId, getCode());
        ResponseEntity apiResp = apiService.confirm(confirmRequest);

        Assertions.assertEquals(
                ( (HashMap<String, String>) apiResp.getBody()).get("operationID"),
                operId
        );
        Assertions.assertTrue(operId.matches("OID_[0-9]+_P"));
        Optional<Operation> oper = apiService.getTransferService().getOperationReporitory().findById(operId);

        Assertions.assertTrue(oper.isPresent());
        Assertions.assertEquals(oper.get().getOperationStatus(), OperationStatus.SUCCESS);
        clearFile();
    }

    private String getCode() {
        String line = null;
        try(
                BufferedReader reader = new BufferedReader(new FileReader("./gen/codes.txt"))
        ) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                line = nextLine;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return line;
    }

    private void clearFile() {
        try {
            new File("./gen/codes.txt").delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}