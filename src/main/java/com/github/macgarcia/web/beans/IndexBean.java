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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.macgarcia.web.enums.CategoriaDivida;
import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.CalculoMensalRepository;
import com.github.macgarcia.web.repository.DividaRepository;
import com.github.macgarcia.web.util.ComponenteDeTela;

import lombok.Getter;
import lombok.Setter;

@Named(value = "indexBean")
@SessionScoped
public class IndexBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Inject
	@Getter
	private DividaRepository dividaRepository;

	@Inject
	private CalculoMensalRepository calculoMensalRepository;

	@Getter
	private List<Divida> dividas;

	@Getter
	@Setter
	private Mes mesSelecionado = Mes.getMesComDigito(LocalDate.now().getMonthValue() - 1);

	@Setter
	private Divida dividaParaManuseio;

	@Getter
	private boolean existeCalculo;

	private Double somatorioTotalDeDividas;

	@Getter
	@Setter
	private String dataDividaString;

	@PostConstruct
	public void init() {
		setDividas();
		existeCalculoMensal();
	}

	/* Processamento inicial da tela */
	public Mes[] getMeses() {
		return ComponenteDeTela.meses();
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

	public void novaDivida() {
		dividaParaManuseio = new Divida();
		dataDividaString = null;
	}

	public void editarDivida(Divida divida) {
		this.dividaParaManuseio = divida;
		this.dataDividaString = formatter.format(dividaParaManuseio.getDataDivida());
	}

	public void excluirDivida(Divida divida) {
		boolean apagou = dividaRepository.apagarEntidade(Divida.class, divida.getId());
		setDividas();
		if (apagou) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro", "Divida foi excluida com sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cadastro", "Erro ao excluir a divida..."));
		}
	}
	/*--*/

	public Divida getDividaParaManuseio() {
		if (Objects.isNull(dividaParaManuseio)) {
			dividaParaManuseio = new Divida();
		}
		return dividaParaManuseio;
	}

	public String getSomatorioTotalDeDividas() {
		return "Valor total: R$ " + new DecimalFormat("#,##0.00").format(somatorioTotalDeDividas);
	}
	/**/

	public void salvarDivida() {
		dividaParaManuseio.setDataDivida(toLocalDate(dataDividaString));
		System.out.println(dividaParaManuseio);
		boolean salvou = dividaRepository.salvarEntidade(dividaParaManuseio);
		setDividas();
		if (salvou) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro", "Divida salva com sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cadastro", "Erro ao salvar divida..."));
		}
		dividaParaManuseio = null;
		dataDividaString = null;
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
		for (Divida d : dividas) {
			somatorioTotalDeDividas += d.getValor();
		}
	}
}
