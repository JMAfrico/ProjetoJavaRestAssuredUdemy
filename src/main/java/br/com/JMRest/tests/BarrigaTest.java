package br.com.JMRest.tests;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.JMRest.core.BaseTest;
import br.com.JMRest.tests.models.Movimentacao;
import br.com.JMRest.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest{

	//private String TOKEN = "";
	private static String CONTA_NOME = "CONTA CORRENTE "+System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOVIMENTACAO_ID;
	private static Integer USUARIO_ID;
	
	@BeforeClass
	public static void login() {	
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
	}
	
	@Test
	public void t02_deveIncluirContaComSucesso() {
		Map<String, String> conta = new HashMap<String, String>();
		conta.put("nome", CONTA_NOME);
			
		CONTA_ID = 
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(201)	
			.extract().path("id")
		;
	}
	
	@Test
	public void t03_deveAlterarContaComSucesso() {	
		Map<String, String> conta = new HashMap<String, String>();
		conta.put("nome", CONTA_NOME +" alterada");
		
		USUARIO_ID =
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
			.body(conta)
		.when()
			.put("/contas/"+CONTA_ID+"")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is(CONTA_NOME +" alterada"))
			.extract().path("usuario_id")
		;
	}
	
	@Test
	public void t04_naoDeveIncluirContaComNomeRepetido() {	
		Map<String, String> conta = new HashMap<String, String>();
		conta.put("nome", CONTA_NOME +" alterada");
		
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(400)	
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {	
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(CONTA_ID);
		movimentacao.setUsuario_id(USUARIO_ID);
		movimentacao.setDescricao("Descricao movimentacao2");
		movimentacao.setEnvolvido("Envolvido na mov");
		movimentacao.setTipo("REC");
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(3));
		movimentacao.setValor(100f);
		movimentacao.setStatus(true);
		
		MOVIMENTACAO_ID = 
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(201)
			.extract().path("id")
		;
	}
	
	@Test
	public void t06_deveValidarCamposObrigatoriosNaMovimentacao() {		
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
			.body("{}")
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
	public void t07_naoDeveInserirMovimentacaoComDataTransacaoFutura() {	
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(CONTA_ID);
		movimentacao.setUsuario_id(USUARIO_ID);
		movimentacao.setDescricao("Descricao movimentacao");
		movimentacao.setEnvolvido("Envolvido na mov");
		movimentacao.setTipo("REC");
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(2));
		movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(5));
		movimentacao.setValor(100f);
		movimentacao.setStatus(true);
		
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
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
	public void t08_naoDeveRemoverContaComMovimentacaoAssociada() {	
	
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.log().all()
			.statusCode(500)	
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void t09_deveCalcularSaldoContas() {	
	
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
		.when()
			.get("/saldo")
		.then()
			.log().all()
			.statusCode(200)	
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"))
		;
	}
	
	@Test
	public void t10_deveRemoverMovimentacao() {	
	
		given()
			.log().all()
			//.header("Authorization", "JWT "+TOKEN)
		.when()
			.delete("/transacoes/"+MOVIMENTACAO_ID+"")
		.then()
			.log().all()
			.statusCode(204)	
		;
	}
	
	@Test
	public void t11_naoDeveAcessarAPISemToken() {
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
