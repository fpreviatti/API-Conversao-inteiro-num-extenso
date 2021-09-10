package com.conversor;

import com.conversor.controller.Controller;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DesafioApplicationTests {

	@Test
	void caseNumeroPositivo() {

		Controller controller = new Controller();

		JSONObject actual = controller.getNumeroExtenso("5");

		JSONObject expected = new JSONObject();
		expected.put("extenso", "cinco");

		Assertions.assertEquals(expected, actual );

	}

	@Test
	void caseNumeroNegativo() {

		Controller controller = new Controller();

		JSONObject actual = controller.getNumeroExtenso("-5");

		JSONObject expected = new JSONObject();
		expected.put("extenso", "menos cinco");

		Assertions.assertEquals(expected, actual );

	}

	@Test
	void caseNumeroZero() {

		Controller controller = new Controller();

		JSONObject actual = controller.getNumeroExtenso("0");

		JSONObject expected = new JSONObject();
		expected.put("extenso", "zero");

		Assertions.assertEquals(expected, actual );

	}

	@Test
	void caseNumeroForaDoIntervalo() {

		Controller controller = new Controller();

		JSONObject actual = controller.getNumeroExtenso("120000");

		JSONObject expected = new JSONObject();
		expected.put("extenso", "valor fora do intervalo [-99999, 99999]");

		Assertions.assertEquals(expected, actual );

	}

	@Test
	void caseValorInvalido() {

		Controller controller = new Controller();

		JSONObject actual = controller.getNumeroExtenso("digitandoletras");

		JSONObject expected = new JSONObject();
		expected.put("extenso", "valor invalido");

		Assertions.assertEquals(expected, actual );

	}
}
