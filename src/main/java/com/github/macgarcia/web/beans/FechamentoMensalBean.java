package com.github.macgarcia.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.enums.ProcessosArmazenados;
import com.github.macgarcia.web.model.CalculoMensal;
import com.github.macgarcia.web.repository.CalculoMensalRepository;

@Named(value = "fechamentoMensalBean")
@SessionScoped
public class FechamentoMensalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CalculoMensalRepository calculoMensalRepository;
	
	private List<CalculoMensal> calculos;
	private Mes mesSelecionado = Mes.getMesComDigito(LocalDate.now().getMonthValue() -1);
	private Double valorRendaMensal;
	
	@PostConstruct
	public void init() {
		setCalculos();
	}
	
	private void setCalculos() {
		this.calculos = calculoMensalRepository
				.getCalculos();
	}
	
	public String telaInicial() {
		return "index?faces-redirect=true";
	}
	
	/* Gets para a tela*/
	public Mes[] getMeses() {
		return Mes.values();
	}
	
	public Mes getMesSelecionado() {
		return mesSelecionado;
	}
	
	public void setMesSelecionado(Mes mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}
	
	public List<CalculoMensal> getCalculos() {
		return calculos;
	}
	
	public void setValorRendaMensal(Double valorRendaMensal) {
		this.valorRendaMensal = valorRendaMensal;
	}
	
	public Double getValorRendaMensal() {
		return valorRendaMensal;
	}
	/**/
	
	/* Comportamentos da tela*/
	public void desfazerFechamento(Integer idCalculo) {
		final Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id_calculo_mensal_p", idCalculo.intValue());
		boolean processou = calculoMensalRepository.executeProcedure(ProcessosArmazenados.DESFAZER_FECHAMENTO_MES, parametros);
		if (processou) {
			setCalculos();
		}
	}
	
	public void executarFechamentoMensal() {
		if (!Objects.isNull(valorRendaMensal)) {
			final Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("mes_selecionado_p", mesSelecionado.name());
			parametros.put("valor_saldo_mensal_p", valorRendaMensal.doubleValue());
			boolean processou = calculoMensalRepository.executeProcedure(ProcessosArmazenados.PROCESSAR_FECHAMENTO_MES, parametros);
			if (processou) {
				setCalculos();
			}
			this.valorRendaMensal = null;
		}
	}
	/**/

}