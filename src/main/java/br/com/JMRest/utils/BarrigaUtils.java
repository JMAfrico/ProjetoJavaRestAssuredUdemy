package br.com.JMRest.utils;

import br.com.JMRest.tests.models.Movimentacao;
import io.restassured.RestAssured;

public class BarrigaUtils {

	public static Integer getIdPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
		
	}
	
	public static Integer getMovimentacaoPelaDescricao(String desc) {
		return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");
		
	}
	
	public static Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(getIdPeloNome("Conta para movimentacoes"));
		//movimentacao.setUsuario_id(USUARIO_ID);
		movimentacao.setDescricao("Descricao movimentacao2");
		movimentacao.setEnvolvido("Envolvido na mov");
		movimentacao.setTipo("REC");
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(3));
		movimentacao.setValor(100f);
		movimentacao.setStatus(true);
		return movimentacao;
	}
	
	public static Movimentacao getMovimentacaoDataFuturaInvalida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(1));
		return movimentacao;
	}
	
	public static Movimentacao getMovimentacaoContaComMovimentacao() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(getIdPeloNome("Conta com movimentacao"));
		return movimentacao;	
	}
}
