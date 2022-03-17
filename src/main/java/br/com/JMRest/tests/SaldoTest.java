package br.com.JMRest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.JMRest.core.BaseTest;
import br.com.JMRest.utils.BarrigaUtils;

public class SaldoTest extends BaseTest{

//	@BeforeClass
//	public static void login() {	
//		System.out.println("-------EFETUANDO LOGIN-------");
//			Map<String, String> login = new HashMap<>();
//			login.put("email","joao_marcossilva@hotmail.com");
//			login.put("senha","123456");		
//			String TOKEN =
//			given()
//				.body(login)
//			.when()
//				.post("/signin")
//			.then()
//				.statusCode(200)	
//				.extract().path("token")
//			;	
//			
//			RestAssured.requestSpecification.header("Authorization", "JWT "+TOKEN);
//			RestAssured.get("/reset").then().statusCode(200);
//	}	
	
	@Test
	public void deveCalcularSaldoContas() {	
		Integer IDCONTA = BarrigaUtils.getIdPeloNome("Conta para saldo");
		
		given()
			.log().all()
		.when()
			.get("/saldo")
		.then()
			.log().all()
			.statusCode(200)	
			.body("find{it.conta_id == "+IDCONTA+"}.saldo", is("534.00"))
		;
	}
}
