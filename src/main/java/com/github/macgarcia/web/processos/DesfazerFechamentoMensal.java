package com.github.macgarcia.web.processos;

import java.io.Serializable;
import java.util.List;

import com.github.macgarcia.web.model.CalculoMensal;
import com.github.macgarcia.web.repository.CalculoMensalRepository;
import com.github.macgarcia.web.repository.DividaRepository;

public class DesfazerFechamentoMensal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> ids;

	public void defazerFechamentoMensal(Integer calculoMensalId, DividaRepository dividaRepository,
			CalculoMensalRepository calculoMensalRepository) {
		this.getListIdsDividasPorCalculoMensal(calculoMensalId, dividaRepository);
		this.updateDasDividasDoFechamentoDesfeito(dividaRepository);
		this.removerCalculoMensalDesfeito(calculoMensalId, calculoMensalRepository);
		this.finalizar();
	}

	private void getListIdsDividasPorCalculoMensal(Integer calculoMensalId, DividaRepository dividaRepository) {
		this.ids = dividaRepository.getListIdsDasDividasDeUmCalculoMensal(calculoMensalId);
	}
	
	private void updateDasDividasDoFechamentoDesfeito(DividaRepository dividaRepository) {
		dividaRepository.updateDasDividasDoFechamentoDesfeito(this.ids);
	}
	
	private void removerCalculoMensalDesfeito(Integer calculoMensalId, CalculoMensalRepository calculoMensalRepository) {
		calculoMensalRepository.apagarEntidade(CalculoMensal.class, calculoMensalId);
	}
	
	private void finalizar() {
		this.ids = null;
	}

}
