package org.home.MoneyTransfer;

import org.home.MoneyTransfer.dto.ApiRequest;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dto.TransferRequestAmount;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoneyTransferApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	//Зарегать тест контейнер с приложением
	// провести тесты
	// удачный перевод в одной валюте и удачный перевод с конвертацией
	// не удачный - ошибка номера карты
	// не удачный - ошибка даты, ошибка баланса
	// не удачный - ошибка номер в карты

	private static final String HOST = "http://localhost";
	private static final int PORT = 5500;
	private static final String IMAGE = "openjdk:8-jdk-alpine";
	private static GenericContainer<?> APP = new GenericContainer<>(IMAGE).withExposedPorts(PORT);

	private final static String ENDPOINT_TRANSFER = "/transfer";
	private final static String ENDPOINT_CONFIRM_OPERATION = "/confirmOperation";
	private final static String[][] CARDS = {
			{"1234123412341234", "02/24", "009", "RUR", "1000"},
			{"4321432143214321", "09/21", "409", "RUR", "560123"},
			{"7894789478947894", "12/22", "567", "USD", "1548"},
			{"7894789111947894", "12/22", "567", "USD", "1548"}, //error number
			{"1234123412341234", "12/18", "567", "RUR", "1548"}, //error date
			{"1234123412341234", "02/24", "555", "RUR", "1548"} //error cvv
	};

	@BeforeAll
	public static void setUp() {
		APP.start();
	}

	@AfterAll
	public static void setDown() {
		APP.stop();
	}

	@Test
	void successSameCurrency() {

		TransferRequest request = new TransferRequest(CARDS[0][0], CARDS[0][1], CARDS[0][2],
				CARDS[1][0],
				new TransferRequestAmount(500.00, "RUR"));

		ResponseEntity<ApiRequest> response = restTemplate.postForEntity(
				HOST + ":" + APP.getMappedPort(PORT) + ENDPOINT_TRANSFER, request, ApiRequest.class);
		String operId = response.getBody().getOperationId();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertTrue(operId.matches("OID_[0-9]+_P"));

		ConfirmRequest confirmRequest = new ConfirmRequest(operId, getCode());
		ResponseEntity<ApiRequest> responseC = restTemplate.postForEntity(
				HOST + ":" + APP.getMappedPort(PORT) + ENDPOINT_CONFIRM_OPERATION, confirmRequest, ApiRequest.class);

		Assertions.assertEquals(HttpStatus.OK, responseC.getStatusCode());
		Assertions.assertEquals(operId, responseC.getBody().getOperationId());
	}

	@Test
	void successDiffCurrency() {
		TransferRequest request = new TransferRequest(CARDS[1][0], CARDS[1][1], CARDS[1][2],
				CARDS[2][0],
				new TransferRequestAmount(300.00, "USD"));

		ResponseEntity<ApiRequest> response = restTemplate.postForEntity(
				HOST + ":" + APP.getMappedPort(PORT) + ENDPOINT_TRANSFER, request, ApiRequest.class);
		String operId = response.getBody().getOperationId();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertTrue(operId.matches("OID_[0-9]+_P"));

		ConfirmRequest confirmRequest = new ConfirmRequest(operId, getCode());
		ResponseEntity<ApiRequest> responseC = restTemplate.postForEntity(
				HOST + ":" + APP.getMappedPort(PORT) + ENDPOINT_CONFIRM_OPERATION, confirmRequest, ApiRequest.class);

		Assertions.assertEquals(HttpStatus.OK, responseC.getStatusCode());
		Assertions.assertEquals(operId, responseC.getBody().getOperationId());
	}

	@Test
	void errorCardNumber() {

		ResponseEntity<ApiRequest> response = sendTransferRequest(3, 2, 100, "USD");

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertEquals(response.getBody().getErrMessageTemp(), "Error input data");

		response = sendTransferRequest(3, 2, 100, "USD");

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertEquals(response.getBody().getErrMessageTemp(), "Error input data");
	}

	@Test
	void errorFromCard() {

		ResponseEntity<ApiRequest> response = sendTransferRequest(4, 1, 100, "RUR");

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertEquals(response.getBody().getErrMessageTemp(), "Error input data");

		response = sendTransferRequest(5, 1, 100, "RUR");

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertEquals(response.getBody().getErrMessageTemp(), "Error input data");
	}

	@Test
	void errorInsufficientFrom() {
		ResponseEntity<ApiRequest> response = sendTransferRequest(1, 3, 1000, "USD");

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertEquals(response.getBody().getErrMessageTemp(), "Error input data");
	}

	private ResponseEntity<ApiRequest> sendTransferRequest(int fromId, int toId, double sum, String currency) {
		TransferRequest request = new TransferRequest(CARDS[fromId][0], CARDS[fromId][1], CARDS[fromId][2],
				CARDS[toId][0],
				new TransferRequestAmount(sum, currency));
		return restTemplate.postForEntity(
				HOST + ":" + APP.getMappedPort(PORT) + ENDPOINT_TRANSFER, request, ApiRequest.class);
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

}
