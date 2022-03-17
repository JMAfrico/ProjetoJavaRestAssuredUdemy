package br.com.JMRest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.JMRest.core.BaseTest;
import br.com.JMRest.tests.models.Movimentacao;
import br.com.JMRest.utils.BarrigaUtils;

public class MovimentacaoTest extends BaseTest{

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
	public void deveInserirMovimentacaoComSucesso() {	
		Movimentacao mov = BarrigaUtils.getMovimentacaoValida();
		
		given()
			.log().all()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(201)
		;
	}
	
	@Test
	public void naoDeveInserirMovimentacaoComDataTransacaoFutura() {	
		Movimentacao movimentacao = BarrigaUtils.getMovimentacaoDataFuturaInvalida();
		
		given()
			.log().all()
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(400)	
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {	
		Movimentacao mov = new Movimentacao();
		given()
			.log().all()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(400)	
			.body("$", hasSize(8))//raiz com 8 msg	
			.body("msg", hasItems(
					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					))
		;
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacaoAssociada() {	
		Movimentacao mov = BarrigaUtils.getMovimentacaoContaComMovimentacao();		
		given()
			.log().all()
			.pathParam("id", mov.getConta_id())
			.body(mov)
		.when()
			.delete("/contas/{id}")
		.then()
			.log().all()
			.statusCode(500)	
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void deveRemoverMovimentacao() {	
		Integer mov = BarrigaUtils.getMovimentacaoPelaDescricao("Movimentacao para exclusao");
		
		given()
			.log().all()
			.pathParam("id", mov)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.log().all()
			.statusCode(204)	
		;
	}
			
}
