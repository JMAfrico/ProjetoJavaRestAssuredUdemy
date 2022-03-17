package br.com.JMRest.tests.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.JMRest.core.BaseTest;
import br.com.JMRest.tests.AutenticacaoTest;
import br.com.JMRest.tests.ContasTest;
import br.com.JMRest.tests.MovimentacaoTest;
import br.com.JMRest.tests.SaldoTest;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AutenticacaoTest.class
})
public class Suite extends BaseTest{

	@BeforeClass
	public static void login() {	
		System.out.println("-------EFETUANDO LOGIN-------");
			Map<String, String> login = new HashMap<>();
			login.put("email","joao_marcossilva@hotmail.com");
			login.put("senha","123456");		
			String TOKEN =
			given()
				.body(login)
			.when()
				.post("/signin")
			.then()
				.statusCode(200)	
				.extract().path("token")
			;			
			RestAssured.requestSpecification.header("Authorization", "JWT "+TOKEN);
			RestAssured.get("/reset").then().statusCode(200);
	}	
}
