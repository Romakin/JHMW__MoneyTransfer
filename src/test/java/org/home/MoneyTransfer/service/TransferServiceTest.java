package org.home.MoneyTransfer.service;

import org.home.MoneyTransfer.dao.Currency;
import org.home.MoneyTransfer.dao.Operation;
import org.home.MoneyTransfer.dao.OperationStatus;
import org.home.MoneyTransfer.dao.PayCard;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dto.TransferRequestAmount;
import org.home.MoneyTransfer.repository.OperationReporitory;
import org.home.MoneyTransfer.repository.PayCardRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransferServiceTest {

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

    @BeforeEach
    void setUp() {
        payCardRepository.saveAndFlush(cards.get(0));
        payCardRepository.saveAndFlush(cards.get(1));
    }

    @AfterEach
    void removeCards() {
        operationReporitory.deleteAll();
        payCardRepository.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        clearFile();
    }

    @Test
    void transfer() {

        TransferRequest request = new TransferRequest(
                cards.get(0).getCardNumber(), cards.get(0).getTillDate(), "567",
                cards.get(1).getCardNumber(),
                new TransferRequestAmount(20.00, "RUR")
        );

        String operId = transferService.transfer(request);

        Optional<Operation> opOper = operationReporitory.findById(operId);

        Assertions.assertTrue(opOper.isPresent());
        Assertions.assertEquals(opOper.get().getOperationStatus(), OperationStatus.PREPARED);
        Assertions.assertEquals(opOper.get().getPayCardFrom(), cards.get(0));
        Assertions.assertEquals(opOper.get().getPayCardTo(), cards.get(1));
    }

    @Test
    void checkValidRequest() {

        boolean success =  transferService.checkValidRequest(new TransferRequest(
                cards.get(0).getCardNumber(), cards.get(0).getTillDate(), "567",
                cards.get(1).getCardNumber(),
                new TransferRequestAmount(20.00, "RUR")
        ));

        boolean error =  transferService.checkValidRequest(new TransferRequest(
                cards.get(0).getCardNumber(), cards.get(0).getTillDate(), "111",
                cards.get(1).getCardNumber(),
                new TransferRequestAmount(20.00, "RUR")
        ));

        Assertions.assertTrue(success);
        Assertions.assertFalse(error);

    }

    static private void clearFile() {
        try {
            new File("./gen/codes.txt").delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}