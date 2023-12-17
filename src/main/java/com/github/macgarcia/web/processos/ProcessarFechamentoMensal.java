package com.github.macgarcia.web.processos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.enums.Situacao;
import com.github.macgarcia.web.model.CalculoMensal;
import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.CalculoMensalRepository;
import com.github.macgarcia.web.repository.DividaRepository;

public class ProcessarFechamentoMensal implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Integer ANO_CORRENTE = LocalDate.now().getYear();

	private List<Divida> dividas;
	private Double valorTotalDeDividas;
	private Double valorResultante;
	private CalculoMensal novoCalculoMensal;

	public void processarFechamentoMensal(Mes mes, Double valorRendaMensal,
			CalculoMensalRepository calculoMensalRepository, DividaRepository dividaRepository) {
		this.existeCalculoMensalParaOMes(mes, calculoMensalRepository);
		this.buscarDividasDoMesSelecionado(mes, dividaRepository);
		this.somatorioDasDividas();
		this.calcularResultado(valorRendaMensal);
		this.montarNovoCalculoMensal(mes, valorRendaMensal);
		this.salvarNovoCalculoMensal(calculoMensalRepository);
		this.finalizar();
	}

	/* Verifica se o mes ja esta fechado */
	private void existeCalculoMensalParaOMes(Mes mes, CalculoMensalRepository calculoMensalRepository) {
		boolean existeCalculoMensal = calculoMensalRepository.existeCalculoMensalParaOMes(mes);
		if (existeCalculoMensal) {
			throw new RuntimeException("Mes indicado já esta fechado...");
		}
	}

	/* Buscar todos os valores das dividas do mes */
	private void buscarDividasDoMesSelecionado(Mes mes, DividaRepository dividaRepository) {
		this.dividas = dividaRepository.todasAsDividasDoMes(mes);
	}

	/* Somatorio dos valores das dividas */
	private void somatorioDasDividas() {
		this.valorTotalDeDividas = this.dividas.stream().mapToDouble(Divida::getValor).sum();
	}

	/* Calculo para saber o saldo */
	private void calcularResultado(Double valorRendaMensal) {
		this.valorResultante = valorRendaMensal - this.valorTotalDeDividas;
	}

	private void montarNovoCalculoMensal(Mes mes, Double valorRendaMensal) {
		this.novoCalculoMensal = new CalculoMensal(mes, valorRendaMensal, this.valorTotalDeDividas,
				this.valorResultante, Situacao.FECHADO, ANO_CORRENTE, dividas);
		novoCalculoMensal.getDividas().forEach(divida -> divida.setCalculoMensal(novoCalculoMensal));
	}

	private void salvarNovoCalculoMensal(CalculoMensalRepository calculoMensalRepository) {
		this.novoCalculoMensal = calculoMensalRepository.salvarEPegarEntidade(novoCalculoMensal);
	}

	private void finalizar() {
		this.dividas = null;
		this.valorTotalDeDividas = null;
		this.valorResultante = null;
		this.novoCalculoMensal = null;
	}

}
