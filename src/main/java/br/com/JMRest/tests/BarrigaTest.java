package br.com.JMRest.tests;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import br.com.JMRest.core.BaseTest;

public class BarrigaTest extends BaseTest{

	private String TOKEN = "";
	
	@Before
	public void login() {	
			Map<String, String> login = new HashMap<>();
			login.put("email","joao_marcossilva@hotmail.com");
			login.put("senha","123456");		
			TOKEN =
			given()
				.body(login)
			.when()
				.post("/signin")
			.then()
				.statusCode(200)	
				.extract().path("token")
			;	
	}
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)	
		;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {	
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body("{ \"nome\":\"conta qualquer\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)	
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {		
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body("{ \"nome\":\"conta qualquer alterada1\" }")
		.when()
			.put("/contas/1115643")
		.then()
			.statusCode(200)
			.body("nome", is("conta qualquer alterada1"))
		;
	}
}
