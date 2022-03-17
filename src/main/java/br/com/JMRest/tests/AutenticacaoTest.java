package br.com.JMRest.tests;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import br.com.JMRest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AutenticacaoTest extends BaseTest {

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
	public void naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		
		given()
			.log().all()
		.when()
			.get("/contas")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
}
