package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.Operation;
import org.home.MoneyTransfer.dao.OperationStatus;
import org.home.MoneyTransfer.dao.PayCard;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dto.TransferRequestAmount;
import org.home.MoneyTransfer.repository.OperationReporitory;
import org.home.MoneyTransfer.repository.PayCardRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfirmServiceTest {

    @Autowired
    private ConfirmService confirmService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private PayCardRepository payCardRepository;
    @Autowired
    private OperationReporitory operationReporitory;

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

    @BeforeEach
    void transfer() {
        payCardRepository.saveAndFlush(cards.get(0));
        payCardRepository.saveAndFlush(cards.get(1));
        TransferRequest request = new TransferRequest(
                cards.get(0).getCardNumber(), cards.get(0).getTillDate(), "567",
                cards.get(1).getCardNumber(),
                new TransferRequestAmount(20.00, "RUR")
        );
        operId = transferService.transfer(request);
    }

    @AfterEach
    void removeCards() {
        operationReporitory.deleteById(operId);
        payCardRepository.delete(cards.get(0));
        payCardRepository.delete(cards.get(1));
    }

    @AfterAll
    static void tearDown() {
        clearFile();
    }

    @Test
    @Order(0)
    void checkValidRequest() {

        ConfirmRequest confirmRequest = new ConfirmRequest(operId, getCode());
        System.out.println(confirmRequest);
        Assertions.assertTrue(confirmService.checkValidRequest(confirmRequest));

    }

    @Test
    @Order(1)
    void confirm() {
        ConfirmRequest confirmRequest = new ConfirmRequest(operId, getCode());
        String respOperId = confirmService.confirm(confirmRequest);

        Assertions.assertEquals(respOperId, operId);

        Optional<Operation> oper = operationReporitory.findById(operId);

        Assertions.assertTrue(oper.isPresent());
        Assertions.assertEquals(oper.get().getOperationStatus(), OperationStatus.SUCCESS);
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

    static void clearFile() {
        try {
            new File("./gen/codes.txt").delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}