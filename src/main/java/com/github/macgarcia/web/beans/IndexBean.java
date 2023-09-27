package com.github.macgarcia.web.beans;

import java.io.Serializable;
import java.text.DecimalFormat;
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
import com.github.macgarcia.web.repository.CalculoMensalRepository;
import com.github.macgarcia.web.repository.DividaRepository;

@Named(value = "indexBean")
@SessionScoped
public class IndexBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	@Inject
	private DividaRepository dividaRepository;
	
	@Inject
	private CalculoMensalRepository calculoMensalRepository;
	
	private List<Divida> dividas;
	private Mes mesSelecionado = Mes.getMesComDigito(LocalDate.now().getMonthValue() -1);
	private Divida dividaParaManuseio;
	private boolean existeCalculo;
	private Double somatorioTotalDeDividas;
	
	private String dataDividaString;

	@PostConstruct
	public void init() {
		setDividas();
		existeCalculoMensal();
	}
	
	/* Processamento inicial da tela*/
	public Mes[] getMeses() {
		return Mes.values();
	}
	
	public CategoriaDivida[] getCategorias() {
		return CategoriaDivida.values();
	}
	
	public void setDividas() {
		dividas = dividaRepository.todasAsDividasDoMes(mesSelecionado);
		somatorioDeDividas();
	}
	
	public void existeCalculoMensal() {
		existeCalculo = calculoMensalRepository.existeFechamentoMensal(mesSelecionado);
	}
	/*--*/
	
	/* Processar eventos da tela */
	public void eventoDeSelecaoDoMes() {
		setDividas();
		existeCalculoMensal();
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
		this.dataDividaString = formatter.format(dividaParaManuseio.getDataDivida());
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
	
	public boolean isExisteCalculo() {
		return existeCalculo;
	}
	
	public String getSomatorioTotalDeDividas() {
		return "R$ " + new DecimalFormat("#,##0.00").format(somatorioTotalDeDividas);
	}
	/**/

	/* Comportamento tela de cadastro*/
	public String telaInicial() {
		return "index?faces-redirect=true";
	}
	
	public String salvarDivida() {
		dividaParaManuseio.setDataDivida(toLocalDate(dataDividaString));
		dividaRepository.salvarEntidade(dividaParaManuseio);
		setDividas();
		dividaParaManuseio = null;
		dataDividaString = null;
		return telaInicial();
	}
	/**/
	
	private LocalDate toLocalDate(final String data) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US);
		try {
			return LocalDate.parse(data, dtf);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/* Somatorio das dividas */
	private void somatorioDeDividas() {
		somatorioTotalDeDividas = 0.0;
		for(Divida d : dividas) {
			somatorioTotalDeDividas += d.getValor();
		}
	}
}
