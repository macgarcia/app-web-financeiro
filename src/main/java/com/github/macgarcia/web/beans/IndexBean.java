package com.github.macgarcia.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.macgarcia.web.enums.CategoriaDivida;
import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.DividaRepository;

@Named(value = "indexBean")
@SessionScoped
public class IndexBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private DividaRepository dividaRepository;
	
	private List<Divida> dividas;
	private Mes mesSelecionado = Mes.getMesComDigito(LocalDate.now().getMonthValue() -1);
	private Divida dividaParaManuseio;
	
	private String dataDividaString;

	@PostConstruct
	public void init() {
		setDividas();
	}
	
	/* Processamento inicial da tela*/
	public Mes[] getMeses() {
		return Mes.values();
	}
	
	public CategoriaDivida[] getCategorias() {
		return CategoriaDivida.values();
	}
	
	private void setDividas() {
		dividas = dividaRepository.todasAsDividasDoMes(mesSelecionado);
	}
	/*--*/
	
	/* Processar eventos da tela */
	public void eventoDeSelecaoDoMes() {
		setDividas();
	}
	
	public String telaDeFechamentoMensal() {
		return "fechamentoMensal?faces-redirect=true";
	}
	
	public String telaDeCadastro() {
		dividaParaManuseio = new Divida();
		dataDividaString = null;
		return "telaCadastro?faces-redirect=true";
	}
	
	public String editarDivida(Divida divida) {
		this.dividaParaManuseio = divida;
		this.dataDividaString = divida.getDataDivida().toString();
		return "telaCadastro?faces-redirect=true";
	}
	
	public void excluirDivida(Divida divida) {
		dividaRepository.apagarEntidade(Divida.class, divida.getId());
		setDividas();
	}
	/*--*/
	
	/* Getter para o JSF */	
	public Mes getMesSelecionado() {
		return mesSelecionado;
	}
	
	public void setMesSelecionado(Mes mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}
	
	public List<Divida> getDividas() {
		return dividas;
	}

	public Divida getDividaParaManuseio() {
		if (Objects.isNull(dividaParaManuseio)) {
			dividaParaManuseio = new Divida();
		}
		return dividaParaManuseio;
	}

	public void setDividaParaManuseio(Divida dividaParaManuseio) {
		this.dividaParaManuseio = dividaParaManuseio;
	}
		
	public String getDataDividaString() {
		return dataDividaString;
	}
	
	public void setDataDividaString(String dataDividaString) {
		this.dataDividaString = dataDividaString;
	}
	/**/

	/* Comportamento tela de cadastro*/
	public String telaInicial() {
		return "index?faces-redirect=true";
	}
	
	public String salvarDivida() {
		dividaParaManuseio.setDataDivida(toLocalDate(dataDividaString));
		System.out.println(dividaParaManuseio);
		dividaRepository.salvarEntidade(dividaParaManuseio);
		setDividas();
		dividaParaManuseio = null;
		dataDividaString = null;
		return telaInicial();
	}
	/**/
	
	private LocalDate toLocalDate(final String data) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		try {
			return LocalDate.parse(data, dtf);
		} catch (Exception e) {
			throw e;
		}
	}
}
