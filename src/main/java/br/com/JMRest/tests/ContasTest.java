package br.com.JMRest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.JMRest.core.BaseTest;
import br.com.JMRest.utils.BarrigaUtils;

public class ContasTest extends BaseTest{

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
	public void deveIncluirContaComSucesso() {
		
		System.out.println("\n<<<<Cenario: Incluir Conta>>>>\n");
		given()
			.log().all()
			.body("{\"nome\": \"Conta inserida\"}")
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(201)	
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {	
		System.out.println("\n<<<<Cenario: Alterar Conta>>>>\n");
		
		Integer CONTA_ID = BarrigaUtils.getIdPeloNome("Conta para alterar");
			
		given()
			.log().all()
			.body("{\"nome\": \"Conta alterada\"}")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is("Conta alterada"))
		;
	}
	
	
	@Test
	public void naoDeveIncluirContaComNomeRepetido() {	
		System.out.println("\n<<<<Cenario: não incluir conta com mesmo nome>>>>\n");
		
		given()
			.log().all()
			.body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(400)	
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void deveRemoverConta() {	
		System.out.println("\n<<<<Cenario: Remover Conta>>>>\n");
		
		Integer CONTA_ID = BarrigaUtils.getIdPeloNome("Conta mesmo nome");
		
		given()
			.log().all()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.log().all()
			.statusCode(204)	
		;
	}
}
